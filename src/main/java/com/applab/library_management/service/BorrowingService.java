package com.applab.library_management.service;

import com.applab.library_management.dto.BorrowBookRequestDTO;
import com.applab.library_management.dto.ReturnBookRequestDTO;
import com.applab.library_management.exception.BookExceptions;
import com.applab.library_management.exception.UserExceptions;
import com.applab.library_management.kafka.KafkaProducerService;
import com.applab.library_management.model.Book;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.model.User;
import com.applab.library_management.repository.BookRepository;
import com.applab.library_management.repository.BorrowingRepository;
import com.applab.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BorrowingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowingService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final Integer borrowingBookDurationDays = 15;

    public Borrowing borrowBook(BorrowBookRequestDTO borrowRequest) {
        User user = userRepository.findById(borrowRequest.getUserId())
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User not found with ID: " + borrowRequest.getUserId()));

        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Book not found with ID: " + borrowRequest.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BookExceptions.BookNotAvailableToBorrowException("No copies available for the book: " + book.getTitle());
        }

        List<Borrowing> existingBorrowings = borrowingRepository.findByUserAndBookAndStatus(
                user, book, Borrowing.BorrowingStatus.BORROWED);
        if (!existingBorrowings.isEmpty()) {
            throw new BookExceptions.BookNotAvailableToBorrowException("You have already borrowed this book");
        }

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create borrowing record
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(borrowingBookDurationDays));
        borrowing.setStatus(Borrowing.BorrowingStatus.BORROWED);

        String message = "Book " + book.getId() + " borrowed by User " + user.getId();
        kafkaProducerService.sendMessage("borrowing-events", message);
        logger.info("Sent Kafka message: {}", message);
        
        return borrowingRepository.save(borrowing);
    }

    public Borrowing returnBook(ReturnBookRequestDTO returnRequest) {
        try {
            logger.info("Starting return book process for borrowing ID: {}", returnRequest.getBorrowingId());
            
            Borrowing borrowing = borrowingRepository.findById(returnRequest.getBorrowingId())
                    .orElseThrow(() -> new BookExceptions.BookNotFoundException("Borrowing record not found"));
            
            logger.info("Found borrowing record: {}", borrowing.getId());

            if (borrowing.getStatus() == Borrowing.BorrowingStatus.RETURNED) {
                throw new BookExceptions.BookNotAvailableToBorrowException("Book is already returned");
            }

            borrowing.setReturnDate(LocalDate.now());
            borrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);
            
            logger.info("Updated borrowing status to RETURNED");

            Book book = borrowing.getBook();
            if (book == null) {
                logger.error("Book is null in borrowing record");
                throw new RuntimeException("Book not found in borrowing record");
            }
            
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);            

            Borrowing savedBorrowing = borrowingRepository.save(borrowing);
            logger.info("Successfully saved borrowing record");
            
            String message = "Book " + book.getId() + " returned by User " + borrowing.getUser().getId();
            kafkaProducerService.sendMessage("borrowing-events", message);
            logger.info("Sent Kafka message: {}", message);

            return savedBorrowing;
        } catch (Exception e) {
            logger.error("Error in returnBook method: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Borrowing> getUserBorrowings(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User not found with ID: " + userId));
        return borrowingRepository.findByUser(user);
    }

    public List<Borrowing> getOverdueBorrowings() {
        return borrowingRepository.findByStatusAndDueDateBefore(
                Borrowing.BorrowingStatus.BORROWED, LocalDate.now());
    }
}
