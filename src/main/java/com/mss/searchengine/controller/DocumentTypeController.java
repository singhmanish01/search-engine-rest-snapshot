package com.mss.searchengine.controller;

import com.mss.searchengine.model.DocumentType;
import com.mss.searchengine.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    private String successmsg = "Document Type Added Successfully";

    @PostMapping("/add-document-type")
    public String addDocumentType(@ModelAttribute("ext") DocumentType doc, Model model){
        documentTypeService.addDocumentType(doc);
        return "redirect:/view-documents";
    }

    @GetMapping("/list-document-types")
    @ResponseBody
    public List<DocumentType> listDocumentTypes(){
        return documentTypeService.listAllDocumentTypes();
    }



}
