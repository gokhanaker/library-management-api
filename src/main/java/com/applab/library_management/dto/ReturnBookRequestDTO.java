package com.applab.library_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReturnBookRequestDTO {
    @NotNull(message = "Borrowing ID is required")
    private UUID borrowingId;
} 