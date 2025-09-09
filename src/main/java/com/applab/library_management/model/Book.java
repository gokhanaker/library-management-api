package com.applab.library_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id", updatable=false, nullable = false)
    private UUID id;

    @NotBlank(message = "Title is required")
    @Column(name="title", nullable = false)
    private String title;

    @NotBlank(message = "ISBN is required")
    @Column(name="isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name="description")
    private String description;

    @Column(name="publication_date")
    private LocalDate publicationDate;

    @NotNull(message = "Total copies is required")
    @Min(value = 0, message = "Total copies must be non-negative")
    @Column(name="total_copies", nullable = false)
    private Integer totalCopies;

    @NotNull(message = "Available copies is required")
    @Min(value = 0, message = "Available copies must be non-negative")
    @Column(name="available_copies", nullable = false)
    private Integer availableCopies;

    @NotBlank(message = "Category is required")
    @Column(name="category", nullable = false)
    private String category;

    @NotBlank(message = "Author is required")
    @Column(name="author_fullname", nullable = false)
    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
