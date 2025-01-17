package com.applab.library_management.service;

import com.applab.library_management.dto.AddBookRequestDTO;
import com.applab.library_management.exception.BookExceptions;
import com.applab.library_management.model.Book;
import com.applab.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(AddBookRequestDTO addBookDTO){
        allowOnlyLibrarianOrAdminForThisBookOperation();

        Optional<Book> existingBook = bookRepository.findByIsbn(addBookDTO.getIsbn());

        if(existingBook.isPresent()){
            return incrementExistingBookNumber(existingBook.get());
        } else {
            return insertNewBook(addBookDTO);
        }
    }

    public Book incrementExistingBookNumber(Book book){
        book.setTotalCopies(book.getTotalCopies()+1);
        book.setAvailableCopies(book.getAvailableCopies()+1);
        book.setUpdatedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }

    public Book insertNewBook(AddBookRequestDTO addBookDTO){
        Book book = new Book();
        book.setTitle(addBookDTO.getTitle());
        book.setIsbn(addBookDTO.getIsbn());
        book.setAuthor(addBookDTO.getAuthor());
        book.setCategory(addBookDTO.getCategory());
        book.setPublicationDate(addBookDTO.getPublicationDate());
        book.setAvailableCopies(1);
        book.setTotalCopies(1);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        return bookRepository.save(book);
    }

    public void allowOnlyLibrarianOrAdminForThisBookOperation(){
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if(!role.equals("ROLE_LIBRARIAN") && !role.equals("ROLE_ADMIN")) {
            throw new BookExceptions.ForbiddenBookException("Forbidden to perform this book operation");
        }
    }

    public List<Book> filterBooks(String title, String author, String category, String isbn) {
        return bookRepository.filterBooks(title, author, category, isbn);
    }

    public void deleteBook(UUID bookId) {
        allowOnlyLibrarianOrAdminForThisBookOperation();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Book not found with ID: " + bookId));

        bookRepository.deleteById(bookId);
    }
}
