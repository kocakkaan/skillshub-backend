package com.reply.skillshub.data.projectcounter;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProjectCounterRepository extends Neo4jRepository<ProjectCounter, String> {

}
