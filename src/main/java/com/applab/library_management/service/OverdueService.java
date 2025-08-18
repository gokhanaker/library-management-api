package com.applab.library_management.service;

import com.applab.library_management.model.Borrowing;
import com.applab.library_management.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OverdueService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    /**
     * Check for overdue books and update their status
     * This method runs daily at 5 AM
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void checkAndUpdateOverdueBooks() {
        List<Borrowing> overdueBorrowings = borrowingRepository.findByStatusAndDueDateBefore(
                Borrowing.BorrowingStatus.BORROWED, LocalDate.now());
        
        for (Borrowing borrowing : overdueBorrowings) {
            borrowing.setStatus(Borrowing.BorrowingStatus.OVERDUE);
            borrowingRepository.save(borrowing);
        }
    }


    public List<Borrowing> getOverdueBooks() {
        return borrowingRepository.findByStatus(Borrowing.BorrowingStatus.OVERDUE);
    }

    public List<Borrowing> getOverdueBooksForUser(UUID userId) {
        return borrowingRepository.findByUserIdAndStatus(userId, Borrowing.BorrowingStatus.OVERDUE);
    }

    public long calculateOverdueDays(Borrowing borrowing) {
        if (borrowing.getStatus() == Borrowing.BorrowingStatus.BORROWED && 
            borrowing.getDueDate().isBefore(LocalDate.now())) {
            return LocalDate.now().toEpochDay() - borrowing.getDueDate().toEpochDay();
        }
        return 0;
    }
} 