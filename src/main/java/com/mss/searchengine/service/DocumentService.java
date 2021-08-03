package com.mss.searchengine.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.searchengine.constants.IndexConstants;
import com.mss.searchengine.dto.DocumentDto;
import com.mss.searchengine.model.Cataloger;
import com.mss.searchengine.model.Document;
import com.mss.searchengine.model.DocumentType;
import com.mss.searchengine.model.Language;
import com.mss.searchengine.repository.DocumentRepository;

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

    public boolean addDocument(DocumentDto documentDto, MultipartFile file) {
        Document document = new Document();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails)auth.getPrincipal()).getUsername();

//        email = jwtRequestFilter.getEmail();
        System.out.println("email is: " + email);
        try(InputStream is = file.getInputStream();
        		BufferedInputStream bf = new BufferedInputStream(is);){
            //byte[] bytes = file.getBytes();
        	String upFile;
            upFile = FileSystems.getDefault().getPath("DOCS").toAbsolutePath()+"/"; //Paths.get("./DOCS" + FileSystems.getDefault().getSeparator()).normalize().toAbsolutePath().toString();
            System.out.println("upload PATH: " + upFile);
            Path path = Paths.get(upFile + file.getOriginalFilename());
            System.out.println("output path: " + path.toString());
            OutputStream os = new FileOutputStream(path.toString());
            byte[] buffer = new byte[4*1024];
            int read;
            while((read = bf.read(buffer,0,buffer.length)) != -1) 
            	os.write(buffer,0,read);
            os.close();
//            Path path = Paths.get("E://KMS" + file.getOriginalFilename());
//            Path path = Paths.get(indexConstants.uploadedDocumentPath + file.getOriginalFilename());
            //Files.write(path, bytes);
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
        if(checkPlagiarism(documentRepository.findAll(),document.getDocumentFilePath())) {
        	document.setAuthorName("invalid file - plagiarism");
        	//try {
				//Files.deleteIfExists(Paths.get(upFile + file.getOriginalFilename()));
				System.out.println("deleted");
			//} catch (IOException e) {
		//		System.out.println(e.getMessage());
			//}
        	return false;        	
        }
        	
        document.setIsIndexed(false);
        
        documentRepository.save(document);
        return true;
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
    
    private boolean checkPlagiarism(Iterable<Document> list,String path) {
    	File orgFile = new File(path);
    	Iterator<Document> iter = list.iterator();
    	while(iter.hasNext()) {
    		File file = new File(iter.next().getDocumentFilePath());
    		try {
				if(FileUtils.contentEquals(orgFile, file))
					return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
    	}
    	return false;
    }
    
}
