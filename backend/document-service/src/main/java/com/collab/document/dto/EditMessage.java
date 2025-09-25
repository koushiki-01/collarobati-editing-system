package com.collab.document.dto;

/**
 * Simple edit message model exchanged over WebSocket:
 * - documentId: id of document
 * - user: username making edit
 * - operation: "insert"|"delete"|"replace"|"full" (we support full content replace for simplicity)
 * - content: content fragment or full content (when operation=full)
 * - cursor / meta fields can be added later
 */
public class EditMessage {
    private Long documentId;
    private String user;
    private String operation;
    private String content;

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
