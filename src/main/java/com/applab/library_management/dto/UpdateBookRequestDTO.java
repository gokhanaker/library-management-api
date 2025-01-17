package com.applab.library_management.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UpdateBookRequestDTO {

    private String title;

    private String isbn;

    private String description;

    private Date publicationDate;

    private String category;

    private String author;

    private Integer totalCopies;

    private Integer availableCopies;
}
