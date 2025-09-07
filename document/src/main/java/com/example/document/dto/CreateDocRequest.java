package com.example.document.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocRequest {
    private String title;
    private String content;
    private Long ownerId;
}
