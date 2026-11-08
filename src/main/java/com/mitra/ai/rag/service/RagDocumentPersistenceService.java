package com.mitra.ai.rag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitra.ai.rag.entity.RagDocument;
import com.mitra.ai.rag.entity.RagDocumentVersion;
import com.mitra.ai.rag.repository.RagDocumentRepository;
import com.mitra.ai.rag.repository.RagDocumentVersionRepository;

@Service
public class RagDocumentPersistenceService {

    private final RagDocumentRepository documentRepository;
    private final RagDocumentVersionRepository versionRepository;

    public RagDocumentPersistenceService(
            RagDocumentRepository documentRepository,
            RagDocumentVersionRepository versionRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional
    public void saveDocumentAndVersion(
            RagDocument document,
            RagDocumentVersion version) {

        documentRepository.save(document);
        versionRepository.save(version);
    }
}