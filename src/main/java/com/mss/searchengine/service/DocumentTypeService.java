package com.mss.searchengine.service;

import com.mss.searchengine.model.DocumentType;
import com.mss.searchengine.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentTypeService {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    public DocumentType findByDocumentType(String documentType){
        Optional<DocumentType> optionalDocumentType = documentTypeRepository.findByDocumentType(documentType);
        return optionalDocumentType.orElse(null);
    }

    public void addDocumentType(DocumentType documentType){
        documentTypeRepository.save(documentType);
    }

    public List<DocumentType> listAllDocumentTypes(){
        return documentTypeRepository.findAll();
    }
}
