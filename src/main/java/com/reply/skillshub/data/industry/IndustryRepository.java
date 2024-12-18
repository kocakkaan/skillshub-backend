package com.reply.skillshub.data.industry;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface IndustryRepository extends Neo4jRepository<Industry, String> {

    List<Industry> findByLabel(String label);

}
