package com.applab.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddBookRequestDTO {

    @NotBlank(message="Title is required")
    @Size(min=1, max=255 , message="Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message="ISBN is required")
    private String isbn;

    private String description;

    private LocalDate publicationDate;

    @NotBlank(message="Category is required")
    private String category;

    @NotBlank(message="Author is required")
    private String author;
}
