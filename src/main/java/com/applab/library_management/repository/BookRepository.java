package com.applab.library_management.repository;

import com.applab.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbn(String isbn);

    @Query(value = "SELECT * FROM books b " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author_fullname) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:category IS NULL OR LOWER(b.category) = LOWER(:category)) " +
            "AND (:isbn IS NULL OR b.isbn = :isbn)",
            nativeQuery = true)
    List<Book> filterBooks(
            @Param("title") String title,
            @Param("author") String author,
            @Param("category") String category,
            @Param("isbn") String isbn
    );
}
