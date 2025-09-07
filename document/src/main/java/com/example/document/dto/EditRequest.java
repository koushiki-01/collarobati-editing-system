package com.example.document.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRequest {
    private Long documentId;
    private String content;
}
