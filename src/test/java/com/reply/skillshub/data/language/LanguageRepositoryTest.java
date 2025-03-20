package com.reply.skillshub.data.language;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class LanguageRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private LanguageRepository languageRepository;

  @Test
  void succesfullySaveLanguage() {
    Language language = Instancio.create(Language.class);
    Language savedLanguage = languageRepository.save(language);
    assertNotNull(savedLanguage.getLanguageAlpha3Code());
  }
  
}
