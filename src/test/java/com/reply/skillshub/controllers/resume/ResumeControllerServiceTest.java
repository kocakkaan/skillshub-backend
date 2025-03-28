package com.reply.skillshub.controllers.resume;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.resume.ResumeService;
import com.reply.skillshub.data.user.UserService;

@ExtendWith(MockitoExtension.class)
public class ResumeControllerServiceTest {

    @InjectMocks
    private ResumeControllerService resumeControllerService;

    @Mock
    private ResumeService resumeService;

    @Mock
    private ExperienceService experienceService;

    @Mock
    private UserService userService;

    @Mock
    private LoadCurrentUser loadCurrentUser;

    // @Test
    // void test_updateResume() {
    //     ResumeDto resumeDto = Instancio.create(ResumeDto.class);
    //     Resume resume = Instancio.create(Resume.class);
    //     doReturn(resume).when(resumeService).findById("1");
    //     doAnswer((invocation) -> invocation.getArgument(0)).when(resumeService).save(any(Resume.class));
    //     resumeControllerService.updateResume("1", resumeDto);
        
    // }

    
}
