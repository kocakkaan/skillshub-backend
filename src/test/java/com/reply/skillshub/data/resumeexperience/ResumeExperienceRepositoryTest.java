package com.reply.skillshub.data.resumeexperience;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class ResumeExperienceRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ResumeExperienceRepository repository;

    @Test
    void test_findAllByResumesId() {
        ResumeExperience resumeExperience = new ResumeExperience();
        ResumeExperience secondResumeExperience = new ResumeExperience();
        repository.save(resumeExperience);
        repository.save(secondResumeExperience);
        // Assertions.assertEquals(2, repository.findAllByResumesId(resume.getId()).size());
    }


}
