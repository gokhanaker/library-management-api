package com.applab.library_management.controller;

import com.applab.library_management.dto.ApiResponse;
import com.applab.library_management.dto.BorrowBookRequestDTO;
import com.applab.library_management.dto.ReturnBookRequestDTO;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<Borrowing>> borrowBook(@Valid @RequestBody BorrowBookRequestDTO borrowRequest) {
        Borrowing borrowing = borrowingService.borrowBook(borrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book borrowed successfully", borrowing));
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<Borrowing>> returnBook(@Valid @RequestBody ReturnBookRequestDTO returnRequest) {
        Borrowing borrowing = borrowingService.returnBook(returnRequest);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", borrowing));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Borrowing>>> getUserBorrowings(@PathVariable UUID userId) {
        List<Borrowing> borrowings = borrowingService.getUserBorrowings(userId);
        return ResponseEntity.ok(ApiResponse.success(borrowings));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<Borrowing>>> getOverdueBorrowings() {
        List<Borrowing> overdueBorrowings = borrowingService.getOverdueBorrowings();
        return ResponseEntity.ok(ApiResponse.success(overdueBorrowings));
    }
}

