package com.reply.skillshub.controllers.experience;

import java.util.List;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeSkillNotFound;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.ExperienceDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceControllerService {

    private final ExperienceService experienceService;

    private final UserService userService;

    public void deleteExperienceById(String id) {
        experienceService.deleteExperienceById(id);
    }

    public ExperienceDto getExperienceById(String id) {
        var potential = experienceService.findById(id, BaseExperience.class)
                .orElseThrow(() -> new ResumeSkillNotFound());
        return ExperienceControllerServiceUtil.convertExperienceToDto(potential);
    }

    public List<ExperienceDto> getExperiencesByUserId(String userId) {
        var user = userService.findById(userId);
        return user.getExperiences().stream().map(ExperienceControllerServiceUtil::convertExperienceToDto).toList();
    }

    public ExperienceDto updateExperienceById(String experienceId, ExperienceDto ExperienceDto) {
        var experience = experienceService.findById(experienceId).orElseThrow(() -> new ResumeSkillNotFound());
        return ExperienceControllerServiceUtil.convertExperienceToDto(
                experienceService.save(
                        ExperienceControllerServiceUtil.updateExperienceWithBaseExperience(experience, ExperienceDto)));
    }

    public ExperienceDto createExperienceForUser(String userId, ExperienceDto ExperienceDto) {
        var user = userService.findById(userId, UserWithExperiences.class);
        Experience experience = new Experience();
        var savedExperience = experienceService
                .save(ExperienceControllerServiceUtil.updateExperienceWithBaseExperience(experience, ExperienceDto));
        user.getExperiences().add(savedExperience);
        userService.save(user);
        return ExperienceControllerServiceUtil.convertExperienceToDto(savedExperience);
    }

}
