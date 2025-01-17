package com.applab.library_management.controller;

import com.applab.library_management.dto.AddBookRequestDTO;
import com.applab.library_management.model.Book;
import com.applab.library_management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(
            @RequestParam(required=false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn
            ) {
        List<Book> books = bookService.filterBooks(title, author, category, isbn);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id) {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
    }
}
