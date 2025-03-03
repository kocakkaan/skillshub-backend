package com.reply.skillshub.data.resume;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ResumeRepository extends Neo4jRepository<Resume, String> {
    
    public <T> Optional<T> findById(String id, Class<T> type);

} 
