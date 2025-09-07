package com.example.document.service;


import com.example.document.dto.CreateDocRequest;
import com.example.document.dto.EditRequest;
import com.example.document.model.Document;
import com.example.document.repository.DocumentRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentService(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    public Document createDocument(CreateDocRequest request) {
        Document doc = new Document();
        doc.setTitle(request.getTitle());
        doc.setContent(request.getContent());
        doc.setOwnerId(request.getOwnerId());
        return documentRepo.save(doc);
    }

    public Optional<Document> getDocument(Long id) {
        return documentRepo.findById(id);
    }

    public Optional<Document> editDocument(EditRequest request) {
        return documentRepo.findById(request.getDocumentId())
                .map(doc -> {
                    doc.setContent(request.getContent());
                    return documentRepo.save(doc);
                });
    }
}
