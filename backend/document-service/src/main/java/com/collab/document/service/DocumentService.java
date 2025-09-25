package com.collab.document.service;

import com.collab.document.entity.Document;
import com.collab.document.repository.DocumentRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap; //24-9 

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;

    // Version-service endpoint
    private final String versionServiceUrl = "http://localhost:8083/api/versions";

     // === LIVE EDIT BUFFER === 24/9
    private final Map<Long, Document> liveBuffer = new ConcurrentHashMap<>();

    public DocumentService(DocumentRepository repository,
                           SimpMessagingTemplate messagingTemplate,
                           RestTemplate restTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
        this.restTemplate = restTemplate;
    }

    /** Create a new document and initial version */
    public Document createDocument(String title, String content, String owner) {
        Document doc = new Document(title, content, owner);
        doc = repository.save(doc);

        // create initial version
        saveVersionSnapshot(doc.getId(), owner);

        return doc;
    }

    public Optional<Document> getDocument(Long id) {
        return repository.findById(id);
    }

    public List<Document> listDocuments() {
        return repository.findAll();
    }

    /**
     * Handles updates via WebSocket or REST API.
     * Avoids duplicate version creation.
     */
    public Document applyFullReplace(Long documentId, String newContent, String user) {
        return applyFullReplace(documentId, newContent, user, false);
    }

    public Document applyFullReplace(Long documentId, String newContent, String user, boolean skipVersion) {
        Document doc = repository.findById(documentId).orElseThrow();
        boolean isContentChanged = !newContent.equals(doc.getContent());

        if (isContentChanged) {
            doc.setContent(newContent);
            doc.setLastModifiedBy(user);
            repository.save(doc);

            // broadcast update
            try {
                messagingTemplate.convertAndSend("/topic/documents/" + documentId, doc);
            } catch (Exception e) {
                System.err.println("WebSocket broadcast failed: " + e.getMessage());
            }

            // create version only if content actually changed
            //saveVersionSnapshot(documentId, user);
            if (!skipVersion) {
                saveVersionSnapshot(documentId, user);
            }
        }

        return doc;
    }

    /** REST API update-content calls this for consistency */
    public Document updateContent(Long documentId, String content, String user) {
        return applyFullReplace(documentId, content, user);
    }

     /** Update content without creating a version (used for revert) */
    public Document updateContentSkipVersion(Long documentId, String content, String user) {
        return applyFullReplace(documentId, content, user, true);
    }

    // === LIVE EDIT METHODS === 24/9
    public Document applyLiveEdit(Long documentId, String newContent, String user) {
        Document doc = repository.findById(documentId).orElseThrow();
        Document liveDoc = new Document(doc.getId(), doc.getTitle(), newContent, user);
        liveDoc.setLastModifiedBy(user);
        liveBuffer.put(documentId, liveDoc);

        try {
            messagingTemplate.convertAndSend("/topic/documents/" + documentId, liveDoc);
        } catch (Exception e) {
            System.err.println("Live WebSocket broadcast failed: " + e.getMessage());
        }

        return liveDoc;
    }

     public Document getLiveContent(Long documentId) {
        return liveBuffer.getOrDefault(documentId,
                repository.findById(documentId).orElseThrow());
    }

    // Commit live buffer as a new version 24/9
    public Document commitLiveVersion(Long documentId, String user) {
        Document liveDoc = liveBuffer.get(documentId);
        if (liveDoc != null) {
            return applyFullReplace(documentId, liveDoc.getContent(), user);
        }
        return repository.findById(documentId).orElseThrow();
    }

    /**
     * Call version-service to create a new version
     */
    private void saveVersionSnapshot(Long documentId, String modifiedBy) {
        Document doc = repository.findById(documentId).orElseThrow();

        // Create JSON payload to match VersionController
        Map<String, Object> payload = Map.of(
            "documentId", documentId,
            "content", doc.getContent(),
            "modifiedBy", modifiedBy
        );

        try {
            restTemplate.postForEntity(versionServiceUrl, payload, Object.class);
        } catch (Exception ex) {
            System.err.println("Failed to call version-service: " + ex.getMessage());
        }
    }
}
