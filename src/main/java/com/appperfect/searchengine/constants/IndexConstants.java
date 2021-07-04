package com.appperfect.searchengine.constants;

import org.springframework.stereotype.Component;

@Component
public class IndexConstants {

    public final String content = "content";
    public final String authorName = "authorName";
    public final String publication = "publication";
    public final String documentName = "documentName";
    public final String language = "language";
    public final String documentId = "documentId";
    public final String indexDir = "./indexFile";//E://KMS//indexFile";
//    public final String indexDir = "/Users/appperfect/Desktop/indexFile";
    public final int topHundredResults = 100;
    public final String uploadedDocumentPath = "/Users/appperfect/Desktop/UploadedFile/";
}
