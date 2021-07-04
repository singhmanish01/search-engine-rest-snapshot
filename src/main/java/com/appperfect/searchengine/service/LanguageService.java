package com.appperfect.searchengine.service;

import com.appperfect.searchengine.model.Language;
import com.appperfect.searchengine.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public Language findByLanguage(String language){
        Optional<Language> optionalLanguage = languageRepository.findByLanguage(language);
        return optionalLanguage.orElse(null);
    }

    public void addLanguage(Language language){
        languageRepository.save(language);
    }

    public List<Language> listAllLanguages(){
        return languageRepository.findAll();
    }
}
