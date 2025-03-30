package com.reply.skillshub.controllers.users.profileextractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.neovisionaries.i18n.LanguageCode;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.language.LanguageService;
import com.reply.skillshub.data.speaks.LanguageLevel;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.services.CvInformation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageExtractorService {

  private final LanguageService languageService;

  public List<Speaks> handleExtractedLanguages(List<CvInformation.Language> languages) {
    List<Speaks> extractedLanguages = new ArrayList<>();
    for (var language : languages) {
      var speaks = new Speaks();
      var languageCode = getLanguageFromText(language.getLanguage());
      if (languageCode == null) {
        continue;
      }

      var optionalLanguage = languageService.findByLanguageCode(languageCode);
      var extractedLanguage = optionalLanguage.orElseGet(() -> {
        var newLanguage = new Language();
        newLanguage.setLanguageCode(languageCode);
        newLanguage.setLanguageAlpha3Code(languageCode.getAlpha3());
        newLanguage.setLanguageName(languageCode.getName());
        return languageService.save(newLanguage);
      });

      speaks.setLanguage(extractedLanguage);
      speaks.setNative(language.getIs_native());
      if (language.getIs_native()) {
        speaks.setLanguageLevel(LanguageLevel.C2);
      } else {
        speaks.setLanguageLevel(LanguageLevel.valueOf(language.getProficiency()));
      }
      
      extractedLanguages.add(speaks);

    }
    return extractedLanguages;
  }

  private LanguageCode getLanguageFromText(String language) {
    var foundLanguageCodes = LanguageCode.findByName(Pattern.compile(language, Pattern.CASE_INSENSITIVE));
    return foundLanguageCodes != null ? foundLanguageCodes.get(0) : null;
  }

}
