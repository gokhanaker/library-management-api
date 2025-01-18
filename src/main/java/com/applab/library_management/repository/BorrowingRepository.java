package com.applab.library_management.repository;

import com.applab.library_management.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {
    List<Borrowing> findByUserId(UUID userId);
    List<Borrowing> findByBookId(UUID bookId);
}
