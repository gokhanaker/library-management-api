package com.applab.library_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BorrowBookRequestDTO {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID bookId;
}
