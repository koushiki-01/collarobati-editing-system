package com.example.version.dto;

import lombok.Data;

@Data
public class SnapshotRequest {
    private Long documentId;
    private String content;
}