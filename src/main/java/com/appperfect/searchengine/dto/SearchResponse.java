package com.appperfect.searchengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private String authorName;
    private String publication;
    private String documentName;
    private String documentId;
    private String language;
}
