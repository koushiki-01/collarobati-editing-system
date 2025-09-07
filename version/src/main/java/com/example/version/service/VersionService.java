package com.example.version.service;

import com.example.version.model.Version;
import com.example.version.repository.VersionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VersionService {

    private final VersionRepo versionRepo;

    public VersionService(VersionRepo versionRepo) {
        this.versionRepo = versionRepo;
    }

    public Version saveSnapshot(Long documentId, String content) {
        Version version = Version.builder()
                .documentId(documentId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return versionRepo.save(version);
    }

    public List<Version> getAllVersionsByDocument(Long documentId) {
        return versionRepo.findByDocumentId(documentId);
    }

    public Optional<Version> getVersion(Long id) {
        return versionRepo.findById(id);
    }
}
