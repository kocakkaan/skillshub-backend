package com.reply.skillshub.data.resume;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.user.User;

@DataNeo4jTest
public class ResumeRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Test
    void succesfullySaveCompany() {
        Resume resume = new Resume();
        Resume savedResume = resumeRepository.save(resume);
        Assertions.assertNotNull(savedResume.getId());
    }

    @Test
    void test_findAllByUsersId() {
        Resume resume = new Resume();
        Resume secondResume = new Resume();
        User user = Instancio.create(User.class);
        resume.getUsers().add(user);
        secondResume.getUsers().add(user);
        resumeRepository.save(resume);
        resumeRepository.save(secondResume);
        Assertions.assertEquals(2, resumeRepository.findAllByUsersId(user.getId()).size());
    }
    
}
