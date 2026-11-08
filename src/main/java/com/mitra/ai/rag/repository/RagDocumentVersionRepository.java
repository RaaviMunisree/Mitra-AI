package com.mitra.ai.rag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitra.ai.rag.entity.RagDocumentVersion;

public interface RagDocumentVersionRepository
        extends JpaRepository<RagDocumentVersion, Long> {

    List<RagDocumentVersion> findByDocumentIdOrderByVersionNumberDesc(
            String documentId);

    boolean existsByDocumentIdAndVersionNumber(
            String documentId, int versionNumber);

    void deleteAllByDocumentId(String documentId);
}