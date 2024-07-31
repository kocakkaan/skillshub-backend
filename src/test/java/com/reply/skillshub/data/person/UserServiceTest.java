package com.reply.skillshub.data.person;

import static org.mockito.Mockito.verify;

import javax.xml.validation.Validator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.data.userrole.UserRole;

import ac.simons.neo4j.migrations.springframework.boot.autoconfigure.MigrationsAutoConfiguration;
import jakarta.validation.Valid;

public class UserServiceTest extends BaseRepositoryTest {

    @Autowired
    private UserService userService;

    @Test
    public void succesfullSave() {
        User savedPerson = userService.save(returnPersonWithEmail());
        Assertions.assertNotNull(savedPerson.getId());

    }

    @Test void failure_PersonWithJustNullValues() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.save(new User()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"lalala"})
    void failure_PersonWithFalseEmailOrNull(String email) {
        User person = returnPersonWithEmail();
        person.setEmail(email);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.save(person));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void failure_PersonWithFirstNameNull(String firstName) {
        User person = returnPersonWithEmail();
        person.setFirstName(firstName);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.save(person));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void failure_PersonWithLastNameNull(String lastName) {
        User person = returnPersonWithEmail();
        person.setLastName(lastName);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.save(person));
    }

    User returnPersonWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        personToSave1.setUserRole(UserRole.ADMIN);
        return personToSave1;
    }


    
}
