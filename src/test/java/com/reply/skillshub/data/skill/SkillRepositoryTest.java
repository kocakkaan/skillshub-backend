package com.reply.skillshub.data.skill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class SkillRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void succesfullySaveSkill() {
        Skill skill = new Skill();
        skill.setLabel("tester");
        Skill savedSkill = skillRepository.save(skill);
        Assertions.assertNotNull(savedSkill.getId());
    }

    @Test
    void findByLabel() {
        Skill skill = new Skill();
        skill.setLabel("tester");
        skillRepository.save(skill);
        Skill foundSkill = skillRepository.findByLabel("tester").orElse(null);
        Assertions.assertNotNull(foundSkill);
    }

    @Test
    void findByLabelIgnoreCase() {
        Skill skill = new Skill();
        skill.setLabel("tester");
        skillRepository.save(skill);
        Skill foundSkill = skillRepository.findByLabelIgnoreCase("TESTER").orElse(null);
        Assertions.assertNotNull(foundSkill);
    }

    @Test
    void findAllByLabel() {
        Skill skill = new Skill();
        skill.setLabel("tester");
        skillRepository.save(skill);
        Skill foundSkill = skillRepository.findAllByLabel("tester").get(0);
        Assertions.assertNotNull(foundSkill);
    }
    
}
