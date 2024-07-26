package com.reply.skillshub;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import ac.simons.neo4j.migrations.springframework.boot.autoconfigure.MigrationsAutoConfiguration;

@Testcontainers
@SpringBootTest
@ImportAutoConfiguration(MigrationsAutoConfiguration.class)
public class BaseRepositoryTest {

    private static Neo4jContainer<?> neo4jContainer;

	@BeforeAll
	static void initializeNeo4j() {
		neo4jContainer = new Neo4jContainer<>()
			.withAdminPassword("somePassword");
		neo4jContainer.start();
	}

	@AfterAll
	static void stopNeo4j() {
		neo4jContainer.close();
	}

	@BeforeEach
	void resetDatabase(@Autowired Neo4jClient client) {
		client.query("MATCH (n) DETACH DELETE n").run();
	}

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }
    
}
