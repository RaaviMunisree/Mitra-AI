package com.mitra.ai.rag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitra.ai.rag.entity.RagDocument;

public interface RagDocumentRepository
        extends JpaRepository<RagDocument, String> {

    List<RagDocument> findAllByOrderByUploadedAtDesc();

    Optional<RagDocument> findByChecksum(String checksum);
}