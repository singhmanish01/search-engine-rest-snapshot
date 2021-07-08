package com.mss.searchengine.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// represents a database table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DOCUMENT")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "AUTHOR_NAME")
    private String authorName;

    @Column(name = "PUBLICATION")
    private String publication;

    @Column(name = "PUBLIC")
    private Boolean isPublic;

    @Column(name = "DOCUMENT_FILE_PATH")
    private String documentFilePath;

    @Column(name = "INDEXED")
    private Boolean isIndexed;

    @ManyToOne()
    @JoinColumn(name = "CATALOGER_ID")
    private Cataloger cataloger;

    @OneToOne()
    @JoinColumn(name = "LANGUAGE_ID")
    private Language language;

    @OneToOne()
    @JoinColumn(name = "DOCUMENT_TYPE")
    private DocumentType documentType;

}