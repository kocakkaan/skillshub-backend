package com.reply.skillshub.data.language;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neovisionaries.i18n.LanguageCode;

@Service
public class LanguageService {

  private final LanguageRepository languageRepository;

  public LanguageService(LanguageRepository languageRepository) {
    this.languageRepository = languageRepository;
  }

  public Language save(Language language) {
    return languageRepository.save(language);
  }

  public Optional<Language> findByLanguageCode(LanguageCode languageCode) {
    return languageRepository.findById(languageCode);
  }
  
}
