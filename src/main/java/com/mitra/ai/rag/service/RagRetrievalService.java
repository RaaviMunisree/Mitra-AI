package com.mitra.ai.rag.service;

import java.util.List;
import java.util.Map;

import com.mitra.ai.rag.dto.RagSearchRequestDTO;
import com.mitra.ai.rag.dto.RagSearchResponseDTO;
import com.mitra.ai.rag.dto.RetrievedChunkDTO;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class RagRetrievalService {

    private static final int DEFAULT_TOP_K = 5;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.65;

    private final VectorStore vectorStore;

    public RagRetrievalService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public RagSearchResponseDTO searchSimilarDocuments(
            RagSearchRequestDTO request) {

        validateRequest(request);

        int topK = request.topK() == null
                ? DEFAULT_TOP_K
                : request.topK();

        double threshold = request.similarityThreshold() == null
                ? DEFAULT_SIMILARITY_THRESHOLD
                : request.similarityThreshold();

        SearchRequest.Builder searchBuilder = SearchRequest.builder()
                .query(request.query().trim())
                .topK(topK)
                .similarityThreshold(threshold);

        if (request.documentId() != null
                && !request.documentId().isBlank()) {

            FilterExpressionBuilder filterBuilder =
                    new FilterExpressionBuilder();

            Filter.Expression filterExpression =
                    filterBuilder.eq(
                            "documentId",
                            request.documentId().trim()
                    ).build();

            searchBuilder.filterExpression(filterExpression);
        }

        List<Document> retrievedDocuments =
                vectorStore.similaritySearch(searchBuilder.build());

        if (retrievedDocuments == null) {
            retrievedDocuments = List.of();
        }

        List<RetrievedChunkDTO> results =
                retrievedDocuments.stream()
                        .map(this::mapToRetrievedChunk)
                        .toList();

        return new RagSearchResponseDTO(
                request.query(),
                results.size(),
                topK,
                threshold,
                results
        );
    }

    private RetrievedChunkDTO mapToRetrievedChunk(
            Document document) {

        Map<String, Object> metadata = document.getMetadata();

        return new RetrievedChunkDTO(
                document.getId(),
                document.getContent(),
                getMetadataString(metadata, "documentId"),
                getMetadataString(metadata, "fileName"),
                getMetadataString(metadata, "fileType"),
                getMetadataString(metadata, "uploadedAt"),
                metadata
        );
    }

    private String getMetadataString(
            Map<String, Object> metadata,
            String key) {

        Object value = metadata.get(key);

        return value == null ? null : value.toString();
    }

    private void validateRequest(RagSearchRequestDTO request) {

        if (request.documentId() != null
                && request.documentId().isBlank()) {
            throw new IllegalArgumentException(
                    "Document ID cannot be blank.");
        }
    }
}