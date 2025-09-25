package com.collab.document.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000, columnDefinition = "TEXT")
    private String content;

    private String owner;  // original creator

    private String lastModifiedBy; // ✅ new field

    public Document() {}

    public Document(String title, String content, String owner) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.lastModifiedBy = owner; // initial same as creator
    }

    // ✅ New constructor to support live editing 24/9
    public Document(Long id, String title, String content, String owner) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.lastModifiedBy = owner; // default
    }

    // ==== getters & setters ====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
}
