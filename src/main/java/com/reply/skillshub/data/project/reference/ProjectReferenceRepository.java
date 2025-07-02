package com.reply.skillshub.data.project.reference;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProjectReferenceRepository extends Neo4jRepository <ProjectReference, String> {

    <T> T findById(String id, Class<T> type);

    <T> Iterable<T> findAllBy(Class<T> type);
  
}