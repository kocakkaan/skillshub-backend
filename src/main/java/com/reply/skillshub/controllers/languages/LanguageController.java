package com.reply.skillshub.controllers.languages;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.neovisionaries.i18n.LanguageCode;
import com.reply.skillshub.openapi.api.LanguagesApi;
import com.reply.skillshub.openapi.model.LanguageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LanguageController implements LanguagesApi {

  @Override
  public ResponseEntity<List<LanguageDto>> languagesGet() {
    var languages = List.of(LanguageCode.values())
        .stream()
        .map(languageCode -> {
          var languageDto = new LanguageDto();
          languageDto.setLanguageCode(languageCode.toString());
          languageDto.setLanguageName(languageCode.getName());
          return languageDto;
        })
        .toList();
    return ResponseEntity.ok(languages);
  }

}
