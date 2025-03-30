package com.reply.skillshub.data.resume;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.services.ValidationHandler;

@SpringBootTest(classes = {ResumeService.class, ValidationHandler.class, TestConfiguration.class})
public class ResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @MockitoBean
    private Neo4jTemplate neo4jTemplate;

    @MockitoBean
    private ResumeRepository resumeRepository;

    @Test
    void successfull_save() {
        Assertions.assertDoesNotThrow(() -> resumeService.save(createResume()));
    }

    ShortCv createResume() {
        ShortCv resume = new ShortCv();
        resume.setTitle("someTitle");
        resume.setBackground("someBackground");
        return resume;
    }
}
