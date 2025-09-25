package com.collab.document.dto;

public class DocumentResponse {
    private Long id;
    private String title;
    private String content;
    private String owner;
    private String lastModifiedBy; 

    public DocumentResponse(Long id, String title, String content, String owner) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
    }

    // after being edited add lastModifiedBy
    public DocumentResponse(Long id, String title, String content, String owner, String lastModifiedBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.lastModifiedBy = lastModifiedBy;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getOwner() { return owner; }
    public String getLastModifiedBy() { return lastModifiedBy; }
}
