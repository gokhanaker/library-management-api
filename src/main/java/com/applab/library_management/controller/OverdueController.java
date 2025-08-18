package com.applab.library_management.controller;

import com.applab.library_management.dto.ApiResponse;
import com.applab.library_management.model.Borrowing;
import com.applab.library_management.service.OverdueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/overdue")
public class OverdueController {

    @Autowired
    private OverdueService overdueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Borrowing>>> getAllOverdueBooks() {
        List<Borrowing> overdueBooks = overdueService.getOverdueBooks();
        return ResponseEntity.ok(ApiResponse.success(overdueBooks));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Borrowing>>> getOverdueBooksForUser(@PathVariable UUID userId) {
        List<Borrowing> overdueBooks = overdueService.getOverdueBooksForUser(userId);
        return ResponseEntity.ok(ApiResponse.success(overdueBooks));
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<String>> checkOverdueBooks() {
        overdueService.checkAndUpdateOverdueBooks();
        return ResponseEntity.ok(ApiResponse.success("Overdue books check completed"));
    }
} 