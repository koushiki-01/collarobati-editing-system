package com.collab.version.dto;

public class RevertMessage {
    private String documentId;
    private String versionId;
    private String user;

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
}

