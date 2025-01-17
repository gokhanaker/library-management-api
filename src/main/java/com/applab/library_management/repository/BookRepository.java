package com.applab.library_management.repository;

import com.applab.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    Optional<Book> findByTitle(String title);

    Optional<List<Book>> findByAuthor(String author);

    Optional<List<Book>> findByCategory(String category);
}
