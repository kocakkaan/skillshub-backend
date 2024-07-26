package com.reply.skillshub.data.person;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.language.bm.Lang;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import com.neovisionaries.i18n.LanguageCode;
import com.neovisionaries.i18n.LocaleCode;
import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.language.Language;

public class PersonRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testPersonWithEmailAndLanguage() {
        Person person = returnPersonWithEmail();
        person.getLanguages().add(returnLanguage());
        personRepository.save(person);

        Person foundPerson = personRepository.findByEmail(person.getEmail()).get();
        Assertions.assertThat(foundPerson.getEmail()).isEqualTo(person.getEmail());

    }

    Person returnPersonWithEmail() {
        Person personToSave1 = new Person();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }

    Language returnLanguage() {
        Language language = new Language();
        LanguageCode languageCode = LanguageCode.de;
        language.setLanguageCode(languageCode);
        language.setLanguageAlpha3Code(languageCode.getAlpha3());
        return language;
    }


    
}
