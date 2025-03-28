package com.reply.skillshub.data.experience;

import static org.instancio.Select.field;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ac.simons.neo4j.migrations.springframework.boot.autoconfigure.MigrationsAutoConfiguration;

@DataNeo4jTest
@ImportAutoConfiguration(MigrationsAutoConfiguration.class)
public class ExperienceRepositoryTest {

    private static Neo4j embeddedDatabaseServer;

    @BeforeAll
    static void initializeNeo4j() {

        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void stopNeo4j() {
        embeddedDatabaseServer.close();
    }

    @Autowired
    private ExperienceRepository experienceRepository;

    @ParameterizedTest
    @MethodSource("requiredParams")
    void testSuccesfullSave(Experience experience) {
        Experience savedExperience = experienceRepository.save(experience);

        Experience foundExperience = experienceRepository.findById(savedExperience.getId()).get();
        Assertions.assertNotNull(foundExperience);
        Assertions.assertNotNull(foundExperience.getId());
    }

    private static Stream<Arguments> requiredParams() {
        var experienceBase = Instancio.of(Experience.class);
        experienceBase.set(field(Experience::getIndustries), List.of());
        experienceBase.set(field(Experience::getLanguages), List.of());
        experienceBase.set(field(Experience::getSkills), List.of());
        experienceBase.set(field(Experience::getOccupation), null);
        experienceBase.set(field(Experience::getEndDate), null);
        return Stream.of(
                Arguments.of(experienceBase.create()));
    }

    // private static Experience experienceWithEmployee() {
    // List<User> users = List.of(Instancio.create(User.class));
    // Instancio.of(Experience.class)
    // .set(Select.field(Experience::getEmployees), users)
    // .set(Select.field(Experience::getId), null);
    // Experience experience = Instancio.create(Experience.class);
    // experience.getEmployees().clear();
    // }

}
