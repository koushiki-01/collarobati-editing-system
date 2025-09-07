package com.example.version.repository;

import com.example.version.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepo extends JpaRepository<Version, Long> {
    List<Version> findByDocumentId(Long documentId);
}