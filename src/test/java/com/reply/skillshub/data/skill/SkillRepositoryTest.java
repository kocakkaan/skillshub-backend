package com.reply.skillshub.data.skill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.user.User;

@DataNeo4jTest
public class SkillRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void succesfullySaveCompany() {
        Skill skill = new Skill();
        skill.setLabel("tester");
        skill.getEmployees().add(returnUserWithEmail());
        Skill savedSkill = skillRepository.save(skill);
        Assertions.assertNotNull(savedSkill.getId());
    }

    User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }
    
}
