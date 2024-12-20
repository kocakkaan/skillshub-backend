package com.reply.skillshub.data.resumeskill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.resume.Resume;

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

  @Test
  void test_findAllByResumesId() {
    ResumeSkill resumeSkill = new ResumeSkill();
    ResumeSkill secondResumeSkill = new ResumeSkill();
    Resume resume = new Resume();
    resumeSkill.getResumes().add(resume);
    secondResumeSkill.getResumes().add(resume);
    resumeSkillRepository.save(resumeSkill);
    resumeSkillRepository.save(secondResumeSkill);
    var resultingSkills = resumeSkillRepository.findAllByResumesId(resume.getId());
    Assertions.assertEquals(2, resultingSkills.size());
  }
  
}
