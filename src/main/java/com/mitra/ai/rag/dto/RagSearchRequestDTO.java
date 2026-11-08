package com.mitra.ai.rag.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RagSearchRequestDTO(

        @NotBlank(message = "Search query cannot be empty")
        @Size(max = 2000, message = "Search query cannot exceed 2000 characters")
        String query,

        @Min(value = 1, message = "Top K must be at least 1")
        @Max(value = 20, message = "Top K cannot exceed 20")
        Integer topK,

        @DecimalMin(value = "0.0")
        @DecimalMax(value = "1.0")
        Double similarityThreshold,

        @Size(max = 100)
        String documentId
) {
}