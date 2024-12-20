package com.reply.skillshub.data.industry;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface IndustryRepository extends Neo4jRepository<Industry, String> {

    List<Industry> findByLabel(String label);

    @Query("MATCH (re:Resume {id: $resumeId}) - [:IN_INDUSTRY] -> (n) RETURN n")
    List<Industry> findAllByResumeId(String resumeId);

}
