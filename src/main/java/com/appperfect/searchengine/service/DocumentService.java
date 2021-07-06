package com.appperfect.searchengine.service;

import com.appperfect.searchengine.constants.IndexConstants;
import com.appperfect.searchengine.dto.DocumentDto;
import com.appperfect.searchengine.model.Cataloger;
import com.appperfect.searchengine.model.Document;
import com.appperfect.searchengine.model.DocumentType;
import com.appperfect.searchengine.model.Language;
import com.appperfect.searchengine.repository.DocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

// interacts between controller and repository
// here we provide validation logic
@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

//    private Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


    @Autowired
    private CatalogerService catalogerService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private IndexConstants indexConstants;

    public Iterable<Document> retrieveAllDocuments() {
        return documentRepository.findAll();
    }

    public DocumentDto getJson(String document, MultipartFile file){
        DocumentDto documentDto = new DocumentDto();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            documentDto = objectMapper.readValue(document, DocumentDto.class);
        }
        catch (IOException e){
            System.out.println("ERROR : " + e.getMessage());
        }
        return documentDto;
    }

    public void addDocument(DocumentDto documentDto, MultipartFile file) {
        Document document = new Document();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails)auth.getPrincipal()).getUsername();

//        email = jwtRequestFilter.getEmail();
        System.out.println("email is: " + email);
        try{
            byte[] bytes = file.getBytes();
            String upFile;
            upFile = FileSystems.getDefault().getPath("DOCS").toAbsolutePath()+"/"; //Paths.get("./DOCS" + FileSystems.getDefault().getSeparator()).normalize().toAbsolutePath().toString();
            System.out.println("upload PATH: " + upFile);
            Path path = Paths.get(upFile + file.getOriginalFilename());
//            Path path = Paths.get("E://KMS" + file.getOriginalFilename());
//            Path path = Paths.get(indexConstants.uploadedDocumentPath + file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println(file.getOriginalFilename() + " file successfully uploaded");
        }
        catch (IOException e){
            System.out.println("ERROR : " + e.getMessage());
        }

        document.setDocumentName(documentDto.getDocumentName());
        document.setAuthorName(documentDto.getAuthorName());
        String upFile = "./DOCS/";
        upFile = FileSystems.getDefault().getPath("DOCS").toAbsolutePath()+"/"; //Paths.get("./DOCS" + FileSystems.getDefault().getSeparator()).normalize().toAbsolutePath().toString();
        System.out.println("upload PATH: " + upFile);
        document.setDocumentFilePath(upFile + file.getOriginalFilename());
//        document.setDocumentFilePath("E://KMS" + file.getOriginalFilename());
//        document.setDocumentFilePath(indexConstants.uploadedDocumentPath + file.getOriginalFilename());
        document.setIsPublic(documentDto.getIsPublic());
        document.setPublication(documentDto.getPublication());

        Cataloger cataloger = catalogerService.findCatalogerByEmail(email);
        document.setCataloger(cataloger);

        Language language = languageService.findByLanguage(documentDto.getLanguage());
        document.setLanguage(language);

        DocumentType documentType = documentTypeService.findByDocumentType(documentDto.getDocumentType());
        document.setDocumentType(documentType);

        document.setIsIndexed(false);

        documentRepository.save(document);
    }

    public List<Document> getAllDocumentByCatalogerId() {
//        String userName = jwtRequestFilter.getEmail();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetails)auth.getPrincipal()).getUsername();
        Cataloger cataloger = catalogerService.findCatalogerByEmail(userName);
        List<Document> document = documentRepository.findAllByCatalogerId(cataloger.getId());
        if(document.isEmpty()){
            return null;
        }
        return document;
    }

    public Boolean findById(long id){
        Optional<Document> optionalDocument =  documentRepository.findById(id);
        return optionalDocument.isPresent();
    }

    public Document findDocumentById(long id){
        Optional<Document> optionalDocument =  documentRepository.findById(id);
        return optionalDocument.orElse(null);
    }

    public void setIndexedFieldTrue(long id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(optionalDocument.isPresent()){
            Document document = optionalDocument.get();
            document.setIsIndexed(true);
            documentRepository.save(document);
        }
    }

}
