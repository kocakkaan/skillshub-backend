package com.reply.skillshub.data.experience;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ExperienceRepository extends Neo4jRepository<Experience, String> {
    
}
