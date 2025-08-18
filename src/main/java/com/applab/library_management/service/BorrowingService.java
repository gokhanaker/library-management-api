package com.applab.library_management.service;

import com.applab.library_management.dto.BorrowBookRequestDTO;
import com.applab.library_management.dto.ReturnBookRequestDTO;
import com.applab.library_management.exception.BookExceptions;
import com.applab.library_management.exception.UserExceptions;
import com.applab.library_management.model.Book;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.model.User;
import com.applab.library_management.repository.BookRepository;
import com.applab.library_management.repository.BorrowingRepository;
import com.applab.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BorrowingService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private UserRepository userRepository;

    private final Integer borrowingBookDurationDays = 15;

    public Borrowing borrowBook(BorrowBookRequestDTO borrowRequest) {
        // Validate user exists
        User user = userRepository.findById(borrowRequest.getUserId())
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User not found with ID: " + borrowRequest.getUserId()));

        // Validate book exists
        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Book not found with ID: " + borrowRequest.getBookId()));

        // Check if book is available
        if (book.getAvailableCopies() <= 0) {
            throw new BookExceptions.BookNotAvailableToBorrowException("No copies available for the book: " + book.getTitle());
        }

        // Check if user has already borrowed this book
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

        return borrowingRepository.save(borrowing);
    }

    public Borrowing returnBook(ReturnBookRequestDTO returnRequest) {
        // Find the borrowing record
        Borrowing borrowing = borrowingRepository.findById(returnRequest.getBorrowingId())
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Borrowing record not found"));

        // Check if book is already returned
        if (borrowing.getStatus() == Borrowing.BorrowingStatus.RETURNED) {
            throw new BookExceptions.BookNotAvailableToBorrowException("Book is already returned");
        }

        // Update borrowing status
        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);

        // Update book availability
        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
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
