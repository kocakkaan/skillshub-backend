package com.reply.skillshub.data.skill;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SkillRepository extends Neo4jRepository<Skill, String> {

    Optional<Skill> findByLabel(String label);

    List<Skill> findAllByLabel(String label);
    
}
