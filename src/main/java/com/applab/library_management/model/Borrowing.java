package com.applab.library_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name="borrowings")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="borrowing_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="user_id", nullable = false)
    private UUID userId;

    @Column(name="book_id", nullable = false)
    private UUID bookId;

    @Column(name="borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name="due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name="return_date")
    private LocalDate returnDate;

    @Column(name="status", nullable = false)
    private String status; // "BORROWED", "RETURNED", "OVERDUE"
}
