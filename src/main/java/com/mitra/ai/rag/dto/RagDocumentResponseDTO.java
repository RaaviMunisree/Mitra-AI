package com.mitra.ai.rag.dto;

import java.time.Instant;

public record RagDocumentResponseDTO(
        String documentId,
        String fileName,
        String fileType,
        int chunkCount,
        String status,
        Instant uploadedAt
) {
}