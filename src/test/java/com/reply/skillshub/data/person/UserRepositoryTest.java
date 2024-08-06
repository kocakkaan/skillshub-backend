package com.reply.skillshub.data.person;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.neovisionaries.i18n.LanguageCode;
import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserRepository;

public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSuccesfullSave() {
        User savedPerson = userRepository.save(returnUserWithEmail());

        Assertions.assertThat(savedPerson.getId()).isNotNull();
    }

    @Test
    void throwsErrorWhenEmailNotUnique() {
        userRepository.save(returnUserWithEmail());
        Assertions
            .assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(() -> userRepository.save(returnUserWithEmail()));
    }

    @Test
    void testPersonWithEmailAndLanguage() {
        User person = returnUserWithEmail();
        person.getSpeaks().add(returnSpeaks());
        userRepository.save(person);

        User foundPerson = userRepository.findByEmail(person.getEmail()).get();
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
        userRepository.save(person);

        List<User> foundList = userRepository.findBySpeaksLanguageLanguageCode(language.getLanguageCode());
        
        Assertions.assertThat(foundList.size()).isEqualTo(1);
    }

    @Test
    void testFindByEmail() {
        User userToSave = returnUserWithEmail();
        userRepository.save(userToSave);

        Optional<User> foundUser = userRepository.findByEmail(userToSave.getEmail());

        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo(userToSave.getEmail());
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
