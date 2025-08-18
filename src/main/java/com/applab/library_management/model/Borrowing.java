package com.applab.library_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name="borrowings")
@EqualsAndHashCode(callSuper = true)
public class Borrowing extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="borrowing_id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Borrow date is required")
    @Column(name="borrow_date", nullable = false)
    private LocalDate borrowDate;

    @NotNull(message = "Due date is required")
    @Column(name="due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name="return_date")
    private LocalDate returnDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private BorrowingStatus status;

    public enum BorrowingStatus {
        BORROWED, RETURNED, OVERDUE
    }
}
