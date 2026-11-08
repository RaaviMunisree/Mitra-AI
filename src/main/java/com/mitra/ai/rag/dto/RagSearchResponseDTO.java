package com.mitra.ai.rag.dto;

import java.util.List;

public record RagSearchResponseDTO(
        String query,
        int totalResults,
        int topK,
        double similarityThreshold,
        List<RetrievedChunkDTO> results
) {
}