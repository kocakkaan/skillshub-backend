package com.reply.skillshub.data.resumeexperience;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.user.User;

@DataNeo4jTest
public class ResumeExperienceRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ResumeExperienceRepository repository;

    @Test
    void test_findAllByResumesId() {
        ResumeExperience resumeExperience = new ResumeExperience();
        ResumeExperience secondResumeExperience = new ResumeExperience();
        Resume resume = new Resume();
        resumeExperience.getResumes().add(resume);
        secondResumeExperience.getResumes().add(resume);
        repository.save(resumeExperience);
        repository.save(secondResumeExperience);
        Assertions.assertEquals(2, repository.findAllByResumesId(resume.getId()).size());
    }


}
