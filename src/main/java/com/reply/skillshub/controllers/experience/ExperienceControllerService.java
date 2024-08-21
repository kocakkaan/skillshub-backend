package com.reply.skillshub.controllers.experience;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ExperienceNotFound;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.openapi.model.BaseExperience;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceControllerService {

    private final ExperienceService experienceService;

    public void deleteExperienceById(String id) {
        experienceService.deleteExperienceById(id);
    }

    public BaseExperience getExperienceById(String id) {
        Experience potential = experienceService.findById(id).orElseThrow(() -> new ExperienceNotFound());
        return convertExperienceToDto(potential);
    }

    public List<BaseExperience> getExperiencesByUserId(String userId) {
        List<Experience> experiences = experienceService.findByUserId(userId);
        throw new UnsupportedOperationException();
    }

    public BaseExperience createExperienceForUser(String userId, BaseExperience baseExperience) {
        throw new UnsupportedOperationException();
    }

    public BaseExperience updateExperienceForUser(String userId, BaseExperience baseExperience) {
        throw new UnsupportedOperationException();
    }

    private BaseExperience convertExperienceToDto(Experience experience) {
        return new BaseExperience()
            .id(experience.getId())
            .industry(convertToIndustry(experience.getIndustry()).orElse(null))
            .responsibilities(experience.getDescriptions())
            .title(experience.getTitle())
            .occupationalCategory(null);
            

    }

    private Optional<com.reply.skillshub.openapi.model.Industry> convertToIndustry(Optional<Industry> industry) {
        return industry.map(i -> new com.reply.skillshub.openapi.model.Industry()
            .id(i.getId())
            .label(i.getLabel()));
    }
    
}
