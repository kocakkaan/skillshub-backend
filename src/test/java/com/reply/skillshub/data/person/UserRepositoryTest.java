package com.reply.skillshub.data.person;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.language.bm.Lang;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import com.neovisionaries.i18n.LanguageCode;
import com.neovisionaries.i18n.LocaleCode;
import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserRepository;

import ac.simons.neo4j.migrations.springframework.boot.autoconfigure.MigrationsAutoConfiguration;

public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository UserRepository;

    @Test
    void testSuccesfullSave() {
        User savedPerson = UserRepository.save(returnUserWithEmail());

        Assertions.assertThat(savedPerson.getId()).isNotNull();
    }

    @Test
    void throwsErrorWhenEmailNotUnique() {
        UserRepository.save(returnUserWithEmail());
        Assertions
            .assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(() -> UserRepository.save(returnUserWithEmail()));
    }

    @Test
    void testPersonWithEmailAndLanguage() {
        User person = returnUserWithEmail();
        person.getSpeaks().add(returnSpeaks());
        UserRepository.save(person);

        User foundPerson = UserRepository.findByEmail(person.getEmail()).get();
        Assertions.assertThat(foundPerson.getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    void testFindByLanguagesLanguageCode() {
        User person = returnUserWithEmail();
        Language language = returnLanguage();
        Speaks speaks = new Speaks();
        speaks.setLanguage(language);
        speaks.setNative(true);
        person.getSpeaks().add(speaks);
        UserRepository.save(person);

        List<User> foundList = UserRepository.findBySpeaksLanguageLanguageCode(language.getLanguageCode());
        
        Assertions.assertThat(foundList.size()).isEqualTo(1);
    }

    User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }

    Speaks returnSpeaks() {
        Speaks speaks = new Speaks();
        speaks.setLanguage(returnLanguage());
        speaks.setNative(true);
        return speaks;
    }

    Language returnLanguage() {
        Language language = new Language();
        LanguageCode languageCode = LanguageCode.de;
        language.setLanguageCode(languageCode);
        language.setLanguageAlpha3Code(languageCode.getAlpha3());
        return language;
    }


    
}
