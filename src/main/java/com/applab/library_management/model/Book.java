package com.applab.library_management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id", updatable=false, nullable = false)
    private UUID id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name="description")
    private String description;

    @Column(name="publication_date")
    private Date publicationDate;

    @Column(name="total_copies", nullable = false)
    private int totalCopies;

    @Column(name="available_copies", nullable = false)
    private int availableCopies;

    @Column(name="category", nullable = false)
    private String category;

    @Column(name="author_fullname", nullable = false)
    private String author;

    @Column(name="created_at")
    private LocalDateTime  createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
