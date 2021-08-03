package com.mss.searchengine.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mss.searchengine.constants.Messages;
import com.mss.searchengine.dto.DocumentDto;
import com.mss.searchengine.dto.SearchResponse;
import com.mss.searchengine.indexingandsearching.SearchIndex;
import com.mss.searchengine.model.Document;
import com.mss.searchengine.model.DocumentType;
import com.mss.searchengine.model.Language;
import com.mss.searchengine.service.CatalogerService;
import com.mss.searchengine.service.DocumentService;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @Autowired
    private CatalogerService catalogerService;

    @Autowired
    private SearchIndex searchIndex;

    @Autowired
    private Messages messages;

    @GetMapping("/showUploadForm")
    public String showUploadForm(Model model){
        model.addAttribute("doc", new DocumentDto());
        return "uplod-form";
    }
    @GetMapping("/view-documents")
    public String retrieveDocumentOfCataloger(Model model){
        Map<String,String> map = new HashMap<>(readMap());
        List<Document> documentList = documentService.getAllDocumentByCatalogerId();
        if(documentList == null)
        	documentList = new ArrayList<>();
        documentList.forEach(d->{
            if(!map.containsKey(Long.toString(d.getId())))
                map.put(Long.toString(d.getId()), "0");
        });
        model.addAttribute("result",documentList);
        model.addAttribute("lang",new Language());
        model.addAttribute("ext",new DocumentType());
        model.addAttribute("map",map);
        return "uploads";
    }

    @PostMapping("/add-document")
    public String addDocument(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file, @ModelAttribute("doc") DocumentDto doc) throws JsonProcessingException {
    	
    	String[] arr = file.getOriginalFilename().split("\\.");
    	System.out.println(Arrays.toString(arr));
    	String ext = arr[arr.length-1];
    	System.out.println(ext);
        doc.setDocumentType(ext);        
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("user: " + ((UserDetails)auth.getPrincipal()).getUsername());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String document = ow.writeValueAsString(doc);
        System.out.println("JSON is: " + document);
        System.out.println("file: " + file.getOriginalFilename()+", type:" + file);

        DocumentDto documentDto = documentService.getJson(document,file);
        boolean plflag =  documentService.addDocument(documentDto, file);
        if(!plflag)
        	return "error-page";
//        return messages.DocumentUploadedMsg;
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/view-documents";
    }
    
    @GetMapping("/search")
    public String searching(@RequestParam String query, Model model) throws Exception{
    	if(query.trim().isEmpty())
    		return "redirect:/";
    	System.out.println(query);
        List<SearchResponse> result = searchIndex.search(query);
        model.addAttribute("result",result);
        return "search-result";
    }

    @GetMapping(value = "/download/document")
    public ResponseEntity<InputStreamResource> download(@RequestParam long documentId) throws Exception{
        Document document = documentService.findDocumentById(documentId);
        File file = new File(document.getDocumentFilePath());
        PathResource pdfFile = new PathResource(document.getDocumentFilePath());
                
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType((new Tika()).detect(file)));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        System.out.println("filename: " + file.getName());
        String CDispo = String.format("attachment; filename=\"%s\"", file.getName());
        headers.add("Content-Disposition", CDispo);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        System.out.println("length is: " + pdfFile.contentLength()+ " path: " + pdfFile.getPath());
        headers.setContentLength(pdfFile.contentLength());
        return new ResponseEntity<InputStreamResource>(
                new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
    }
    
    public Map<String, String> readMap() {
        Map<String, String> map = new HashMap<>();
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream("count.dat")))) {

            try {
                map = (Map<String, String>) locFile.readObject();
//                    System.out.println("Read location: "+loc.getLocationId()+": "+loc.getDescription());
//                    System.out.println("Found "+(loc.getExits().size()-1)+" exits");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(" something gone wrong " + e.getMessage());
            return new HashMap<>();
        }
        return map;
    }
//    @PostMapping("/edit-document")
//    public ResponseEntity<?> editDocument(long documentId){
//
//    }
    
}
