package com.mitra.ai.rag.controller;

import java.util.List;

import com.mitra.ai.rag.dto.RagDocumentResponseDTO;
import com.mitra.ai.rag.service.RagDocumentManagementService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag/documents")
public class RagManagementController {

    private final RagDocumentManagementService managementService;

    public RagManagementController(
            RagDocumentManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping
    public ResponseEntity<List<RagDocumentResponseDTO>> getAllDocuments() {

        return ResponseEntity.ok(
                managementService.getAllDocuments());
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<RagDocumentResponseDTO> getDocumentById(
            @PathVariable String documentId) {

        return ResponseEntity.ok(
                managementService.getDocumentById(documentId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId) {

        managementService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}