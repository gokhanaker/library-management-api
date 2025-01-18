package com.applab.library_management.service;

import com.applab.library_management.dto.BorrowBookRequestDTO;
import com.applab.library_management.exception.BookExceptions;
import com.applab.library_management.model.Book;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.repository.BookRepository;
import com.applab.library_management.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowingService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

    private final Integer borrowingBookDurationDays = 15;

    public Borrowing borrowBook(BorrowBookRequestDTO borrowRequest) {


        Book book = bookRepository.findById(borrowRequest.getBookId()).orElseThrow(() ->
                new BookExceptions.BookNotFoundException("Book not found with ID: " + borrowRequest.getBookId()));

        if(book.getAvailableCopies() <= 0){
            throw new BookExceptions.BookNotAvailableToBorrowException("No copies available for the book with ID: " + borrowRequest.getBookId());
        }

        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setUserId(borrowRequest.getUserId());
        borrowing.setBookId(borrowRequest.getBookId());
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(borrowingBookDurationDays));
        borrowing.setStatus("BORROWED");

        return borrowingRepository.save(borrowing);
    }
}
