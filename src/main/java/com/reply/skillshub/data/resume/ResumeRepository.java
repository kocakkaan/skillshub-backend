package com.reply.skillshub.data.resume;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ResumeRepository extends Neo4jRepository<Resume, String> {

    
} 
