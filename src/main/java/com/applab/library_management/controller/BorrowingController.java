package com.applab.library_management.controller;

import com.applab.library_management.dto.BorrowBookRequestDTO;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<Borrowing> borrowBook(@Valid @RequestBody BorrowBookRequestDTO borrowRequest) {
        Borrowing borrowing = borrowingService.borrowBook(borrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }
}

