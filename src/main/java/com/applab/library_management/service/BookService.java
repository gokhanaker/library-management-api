package com.applab.library_management.service;

import com.applab.library_management.dto.AddBookRequestDTO;
import com.applab.library_management.dto.UpdateBookRequestDTO;
import com.applab.library_management.exception.BookExceptions;
import com.applab.library_management.model.Book;
import com.applab.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Book not found with ID: " + bookId));
    }

    public void deleteBook(UUID bookId) {
        allowOnlyLibrarianOrAdminForThisBookOperation();
        bookRepository.findById(bookId)
                .orElseThrow(() -> new BookExceptions.BookNotFoundException("Book not found with ID: " + bookId));

        bookRepository.deleteById(bookId);
    }

    public Book updateBook(UUID bookId, UpdateBookRequestDTO updateDTO) {
        allowOnlyLibrarianOrAdminForThisBookOperation();

        Book existingBook = bookRepository.findById(bookId).orElseThrow(() ->
                new BookExceptions.BookNotFoundException("Book not found with ID: " + bookId));

        if (updateDTO.getTitle() != null && !updateDTO.getTitle().isEmpty()) {
            existingBook.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null && !updateDTO.getDescription().isEmpty()) {
            existingBook.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getAuthor() != null && !updateDTO.getAuthor().isEmpty()) {
            existingBook.setAuthor(updateDTO.getAuthor());
        }
        if (updateDTO.getCategory() != null && !updateDTO.getCategory().isEmpty()) {
            existingBook.setCategory(updateDTO.getCategory());
        }
        if (updateDTO.getPublicationDate() != null) {
            existingBook.setPublicationDate(updateDTO.getPublicationDate());
        }
        if (updateDTO.getTotalCopies() != null && updateDTO.getTotalCopies() >= 0) {
            existingBook.setTotalCopies(updateDTO.getTotalCopies());
        }
        if (updateDTO.getAvailableCopies() != null & updateDTO.getAvailableCopies() >= 0) {
            existingBook.setAvailableCopies(updateDTO.getAvailableCopies());
        }

        return bookRepository.save(existingBook);
    }
}
