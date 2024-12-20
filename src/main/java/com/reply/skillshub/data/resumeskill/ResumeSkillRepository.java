package com.reply.skillshub.data.resumeskill;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ResumeSkillRepository extends Neo4jRepository<ResumeSkill, String> {

  List<ResumeSkill> findAllByResumesId(String resumeId);
}