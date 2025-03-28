package com.reply.skillshub.data.experience;

import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.user.User;

@SpringBootTest(classes = {ExperienceService.class, ValidationHandler.class, TestConfiguration.class})
public class ExperienceServiceTest {

    @Autowired
    private ExperienceService experienceService;

    @MockitoBean
    private ExperienceRepository experienceRepository;

    @Test
    void succesfullySaveExperience() {
        Assertions.assertDoesNotThrow(() -> experienceService.save(returnValidExperience()));
    }

    @ParameterizedTest
    @MethodSource("failingObjects")
    void shouldFail(Experience experience) {

        Assertions
            .assertThrows(
                ValidationException.class, 
                () -> experienceService.save(experience)
            );

    }

    private static Stream<Arguments> failingObjects() {

        return Stream.of(
            Arguments.of(returnExperienceWithEmptyTitle()),
            Arguments.of(returnExperienceWithNullTitle())
        );
    }

    private static Experience returnValidExperience() {
        Experience experience = new Experience();
        experience.setTitle("Test");
        experience.setOccupation(Instancio.create(Occupation.class));
        return experience;
    }


    private static Experience returnExperienceWithEmptyTitle() {
        Experience experience = returnValidExperience();
        experience.setTitle("");
        return experience;
    }

    private static Experience returnExperienceWithNullTitle() {
        Experience experience = returnValidExperience();
        experience.setTitle(null);
        return experience;
    }

    private static User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }







    
}
