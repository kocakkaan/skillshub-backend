package com.reply.skillshub.data.experience;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ExperienceRepository extends Neo4jRepository<Experience, String> {
    
    <T> Optional<T> findById(String id, Class<T> type);

}
