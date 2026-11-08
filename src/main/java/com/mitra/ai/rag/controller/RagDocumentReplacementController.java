package com.mitra.ai.rag.controller;

import com.mitra.ai.rag.dto.DocumentIngestionResponseDTO;
import com.mitra.ai.rag.service.RagDocumentReplacementService;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rag/documents")
public class RagDocumentReplacementController {

    private final RagDocumentReplacementService replacementService;

    public RagDocumentReplacementController(
            RagDocumentReplacementService replacementService) {
        this.replacementService = replacementService;
    }

    @PutMapping(
            value = "/{documentId}/versions",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentIngestionResponseDTO> replaceDocument(
            @PathVariable @NotBlank String documentId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                replacementService.replaceDocument(documentId, file));
    }
}