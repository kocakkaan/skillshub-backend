package com.reply.skillshub.data.resume;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.services.ValidationHandler;

@SpringBootTest(classes = {ResumeService.class, ValidationHandler.class, TestConfiguration.class})
public class ResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @MockBean
    private ResumeRepository resumeRepository;

    @Test
    void successfull_save() {
        Assertions.assertDoesNotThrow(() -> resumeService.save(createResume()));
    }

    Resume createResume() {
        Resume resume = new Resume();
        resume.setTitle("someTitle");
        resume.setBackground("someBackground");
        return resume;
    }
}
