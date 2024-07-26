package com.reply.skillshub.data.person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository extends Neo4jRepository<Person, String> {

    List<Person> findByFirstName(String firstname);

    List<Person> findByLastName(String lastname);

    Optional<Person> findByEmail(String email);
    
}
