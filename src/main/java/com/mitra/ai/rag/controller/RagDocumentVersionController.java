package com.mitra.ai.rag.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mitra.ai.rag.dto.RagDocumentVersionResponseDTO;
import com.mitra.ai.rag.service.RagDocumentVersionService;

@RestController
@RequestMapping("/api/rag/documents")
public class RagDocumentVersionController {

    private final RagDocumentVersionService versionService;

    public RagDocumentVersionController(
            RagDocumentVersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/{documentId}/versions")
    public ResponseEntity<List<RagDocumentVersionResponseDTO>> getVersionHistory(
            @PathVariable String documentId) {

        return ResponseEntity.ok(
                versionService.getVersionHistory(documentId));
    }
}