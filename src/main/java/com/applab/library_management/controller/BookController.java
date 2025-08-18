package com.applab.library_management.controller;

import com.applab.library_management.dto.AddBookRequestDTO;
import com.applab.library_management.dto.ApiResponse;
import com.applab.library_management.dto.UpdateBookRequestDTO;
import com.applab.library_management.model.Book;
import com.applab.library_management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<Book>> addBook(@Valid @RequestBody AddBookRequestDTO addBookDTO){
        Book savedBook = bookService.addBook(addBookDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book added successfully", savedBook));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Book>>> filterBooks(
            @RequestParam(required=false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn
            ) {
        List<Book> books = bookService.filterBooks(title, author, category, isbn);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Book>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable UUID id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", "Book with ID " + id + " has been deleted"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequestDTO updateBookDTO) {
        Book updatedBook = bookService.updateBook(id, updateBookDTO);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", updatedBook));
    }
}
