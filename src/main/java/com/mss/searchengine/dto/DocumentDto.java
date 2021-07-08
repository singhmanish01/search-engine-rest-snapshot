package com.mss.searchengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private String documentName;
    private String authorName;
    private String publication;
    private Boolean isPublic;
    private String language;
    private String documentType;
}
