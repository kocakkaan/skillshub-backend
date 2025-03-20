package com.reply.skillshub.data.occupation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface OccupationRepository extends Neo4jRepository<Occupation, String> {

    Optional<Occupation> findByLabel(String label);

    List<Occupation> findAllByLabel(String label);

    Optional<Occupation> findByLabelIgnoreCase(String label);
    
}
