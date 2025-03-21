package com.reply.skillshub.data.resumeskill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class ResumeSkillRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private ResumeSkillRepository resumeSkillRepository;

  @Test
  void succesfullySaveResumeSkill() {
    ResumeSkill resumeSkill = new ResumeSkill();
    ResumeSkill savedResumeSkill = resumeSkillRepository.save(resumeSkill);
    Assertions.assertNotNull(savedResumeSkill.getId());
  }

}
