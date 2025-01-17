package com.applab.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddBookRequestDTO {

    @NotBlank(message="Username is required")
    @Size(min=5, max=50 , message="Username must be between 5 and 50 characters")
    private String title;

    @NotBlank(message="ISBN is required")
    private String isbn;

    private String description;

    private Date publicationDate;

    @NotBlank(message="Category is required")
    private String category;

    @NotBlank(message="Author is required")
    private String author;
}
