package com.collab.version.service;

import com.collab.version.entity.Version;
import com.collab.version.repository.VersionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
//import java.util.Map;

@Service
public class VersionService {

    private final VersionRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;  

        // document-service base URL
    private final String documentServiceUrl = "http://localhost:8082/api/documents";

    public VersionService(VersionRepository repository, SimpMessagingTemplate messagingTemplate, RestTemplate restTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
        this.restTemplate=restTemplate;
    }

    /**
     * Creates a new version and broadcasts it to WebSocket clients
     */
    public Version createVersion(Long documentId, String content, String modifiedBy) {
        Version version = new Version(documentId, content, modifiedBy);
        Version saved = repository.save(version);

        // Broadcast via WebSocket
        messagingTemplate.convertAndSend("/topic/versions/" + documentId, saved);
        return saved;
    }

    /**
     * Get all versions for a document
     */
    public List<Version> getVersionsByDocumentId(Long documentId) {
        return repository.findByDocumentIdOrderByTimestampDesc(documentId);
    }

    /**
     * Revert a document to an old version
     */
    public Version revertToVersion(Long versionId, String revertedBy) {
        Version oldVersion = repository.findById(versionId)
            .orElseThrow(() -> new RuntimeException("Version not found"));

        // create a new version with old content
    //     Version newVersion = createVersion(
    //         oldVersion.getDocumentId(),
    //         oldVersion.getContent(),
    //         revertedBy
    // );

        // Update document-service to reflect reverted content
        try {
            String url = documentServiceUrl + "/" + oldVersion.getDocumentId() + "/update-content?user=" + revertedBy;

            // Send old content as plain String in request body
            restTemplate.postForEntity(url, oldVersion.getContent(), Void.class);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to update document-service during revert: " + e.getMessage());
        }

        //return newVersion;
         List<Version> versions = repository.findByDocumentIdOrderByTimestampDesc(oldVersion.getDocumentId());
        return versions.get(0); 
    }
}
