package com.reply.skillshub.data.experience;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ExperienceRepository extends Neo4jRepository<Experience, String> {
    
    List<Experience> findAllByEmployeesId(String userId);

}
