package com.reply.skillshub.data.project;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProjectRepository extends Neo4jRepository<Project, String> {
    
    public <T> Optional<T> findById(String id, Class<T> type);

} 
