package com.reply.skillshub.data.person;

import static org.mockito.Mockito.verify;

import javax.xml.validation.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.reply.skillshub.BaseRepositoryTest;

import jakarta.validation.Valid;

public class PersonServiceTest extends BaseRepositoryTest {

    @Autowired
    private PersonService personService;

    @Test
    public void succesfullSave() {
        Person savedPerson = personService.save(returnPersonWithEmail());
        Assertions.assertNotNull(savedPerson.getId());
    }

    @Test void failure_PersonWithJustNullValues() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.save(new Person()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"lalala"})
    void failure_PersonWithFalseEmailOrNull(String email) {
        Person person = returnPersonWithEmail();
        person.setEmail(email);
        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.save(person));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void failure_PersonWithFirstNameNull(String firstName) {
        Person person = returnPersonWithEmail();
        person.setFirstName(firstName);
        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.save(person));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void failure_PersonWithLastNameNull(String lastName) {
        Person person = returnPersonWithEmail();
        person.setLastName(lastName);
        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.save(person));
    }

    Person returnPersonWithEmail() {
        Person personToSave1 = new Person();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }


    
}
