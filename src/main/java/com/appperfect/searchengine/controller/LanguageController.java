package com.appperfect.searchengine.controller;

import com.appperfect.searchengine.model.DocumentType;
import com.appperfect.searchengine.model.Language;
import com.appperfect.searchengine.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    private String successmsg = "Language Added Successfully";

    @PostMapping("/add-language")
    public String addLanguage(@ModelAttribute("lang")Language lang, Model model){
        languageService.addLanguage(lang);
//        return successmsg;
        return "redirect:/view-documents";
    }

    @GetMapping("/list-languages")
    public List<Language> listLanguages(){
        return languageService.listAllLanguages();
    }
}