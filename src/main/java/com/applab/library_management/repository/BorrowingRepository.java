package com.applab.library_management.repository;

import com.applab.library_management.model.Book;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {
    List<Borrowing> findByUserId(UUID userId);
    List<Borrowing> findByBookId(UUID bookId);
    List<Borrowing> findByUser(User user);
    List<Borrowing> findByUserAndBookAndStatus(User user, Book book, Borrowing.BorrowingStatus status);
    List<Borrowing> findByStatusAndDueDateBefore(Borrowing.BorrowingStatus status, LocalDate date);
    List<Borrowing> findByStatus(Borrowing.BorrowingStatus status);
    List<Borrowing> findByUserIdAndStatus(UUID userId, Borrowing.BorrowingStatus status);
    
    Long countByStatus(Borrowing.BorrowingStatus status);
}
