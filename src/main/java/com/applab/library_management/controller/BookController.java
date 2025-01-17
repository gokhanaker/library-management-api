package com.applab.library_management.controller;

import com.applab.library_management.dto.AddBookRequestDTO;
import com.applab.library_management.model.Book;
import com.applab.library_management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@Valid @RequestBody AddBookRequestDTO addBookDTO){
        Book savedBook = bookService.addBook(addBookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
}
