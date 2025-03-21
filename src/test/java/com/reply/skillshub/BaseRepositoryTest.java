package com.reply.skillshub;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ac.simons.neo4j.migrations.springframework.boot.autoconfigure.MigrationsAutoConfiguration;

// @Testcontainers
@ImportAutoConfiguration(MigrationsAutoConfiguration.class)
public class BaseRepositoryTest {

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

	// @AfterAll
	// static void stopNeo4j() {

	// embeddedDatabaseServer.close();
	// }

	// @Container
	// private static Neo4jContainer<?> neo4jContainer = new
	// Neo4jContainer<>("neo4j:5.21.0").withAdminPassword("somePassword");

	// private static Neo4jContainer<?> neo4jContainer;

	// @BeforeAll
	// static void initializeNeo4j() {
	// if (neo4jContainer == null) {
	// neo4jContainer = new Neo4jContainer<>("neo4j:5.21.0")
	// .withAdminPassword("somePassword");
	// neo4jContainer.start();
	// }

	// }

	@BeforeEach
	void resetDatabase(@Autowired Neo4jClient client) {
		client.query("MATCH (n) DETACH DELETE n").run();
	}

	// @DynamicPropertySource
	// static void neo4jProperties(DynamicPropertyRegistry registry) {

	// registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
	// registry.add("spring.neo4j.authentication.username", () -> "neo4j");
	// registry.add("spring.neo4j.authentication.password",
	// neo4jContainer::getAdminPassword);
	// }

}
