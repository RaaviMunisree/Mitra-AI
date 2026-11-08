package com.mitra.ai.rag.controller;

import com.mitra.ai.rag.dto.RagSearchRequestDTO;
import com.mitra.ai.rag.dto.RagSearchResponseDTO;
import com.mitra.ai.rag.service.RagRetrievalService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagSearchController {

    private final RagRetrievalService retrievalService;

    public RagSearchController(
            RagRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @PostMapping("/search")
    public ResponseEntity<RagSearchResponseDTO> searchDocuments(
            @Valid @RequestBody RagSearchRequestDTO request) {

        return ResponseEntity.ok(
                retrievalService.searchSimilarDocuments(request));
    }
}