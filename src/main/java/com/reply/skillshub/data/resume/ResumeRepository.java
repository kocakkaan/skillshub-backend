package com.reply.skillshub.data.resume;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import jakarta.transaction.Transactional;

public interface ResumeRepository extends Neo4jRepository<ShortCv, String> {
    
    public <T> Optional<T> findById(String id, Class<T> type);

    @Transactional
    @Query("""
            MATCH (s:ShortCv {id: $shortCvId})
            MATCH (u:User {id: $userId})
            MERGE (u)-[:HAS_RESUME]->(s)
            RETURN s
            """)
    public ShortCv linkShortCvWithUser(String userId, String shortCvId);

} 
