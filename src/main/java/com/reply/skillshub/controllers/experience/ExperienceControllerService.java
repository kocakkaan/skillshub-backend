package com.reply.skillshub.controllers.experience;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ExperienceNotFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.BaseExperience;
import com.reply.skillshub.openapi.model.OccupationalCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceControllerService {

    private final ExperienceService experienceService;

    private final UserService userService;

    public void deleteExperienceById(String id) {
        experienceService.deleteExperienceById(id);
    }

    public BaseExperience getExperienceById(String id) {
        Experience potential = experienceService.findById(id).orElseThrow(() -> new ExperienceNotFound());
        return convertExperienceToDto(potential);
    }

    public List<BaseExperience> getExperiencesByUserId(String userId) {
        List<Experience> experiences = experienceService.findByUserId(userId);
        return experiences.stream().map(this::convertExperienceToDto).toList();
    }

    public BaseExperience updateExperienceById(String experienceId, BaseExperience baseExperience) {
        Experience experience = experienceService.findById(experienceId).orElseThrow(() -> new ExperienceNotFound());
        return convertExperienceToDto(experienceService.save(updateExperienceWithBaseExperience(experience, baseExperience)));
    }

    public BaseExperience createExperienceForUser(String userId, BaseExperience baseExperience) {
        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFound());
        Experience experience = new Experience();
        experience.setEmployees(List.of(user));
        return convertExperienceToDto(experienceService.save(updateExperienceWithBaseExperience(experience, baseExperience)));
    }
    private Experience updateExperienceWithBaseExperience(Experience experience, BaseExperience baseExperience) {
        experience.setTitle(baseExperience.getTitle());
        experience.setDescriptions(baseExperience.getResponsibilities());
        experience.setIndustries(convertToIndustries(baseExperience.getIndustry()));
        experience.setSkills(convertToSkillEntityList(baseExperience.getSkills()));
        experience.setOccupation(convertToOccupation(baseExperience.getOccupationalCategory()));
        baseExperience.getStartDate().ifPresent((date) -> experience.setStartDate(date));
        baseExperience.getEndDate().ifPresent((date) -> experience.setEndDate(date));
        return experience;
    }
    
    private List<Occupation> convertToOccupation(OccupationalCategory occupationalCategory) {
        Occupation occupation = new Occupation();
        occupation.setId(occupationalCategory.getId());
        occupation.setLabel(occupationalCategory.getLabel());
        return List.of(occupation);
    }

    private BaseExperience convertExperienceToDto(Experience experience) {
        return new BaseExperience()
            .id(experience.getId())
            .industry(convertToIndustryDto(experience.getIndustry()).orElse(null))
            .responsibilities(experience.getDescriptions())
            .title(experience.getTitle())
            .occupationalCategory(null)
            .skills(convertToSkillDtoList(experience.getSkills()));
    }

    private com.reply.skillshub.openapi.model.Skill convertToSkillDto(Skill skill) {
        return new com.reply.skillshub.openapi.model.Skill()
            .id(skill.getId())
            .label(skill.getLabel());
    }

    private List<com.reply.skillshub.openapi.model.Skill> convertToSkillDtoList(List<Skill> skills) {
        return skills.stream()
            .map(this::convertToSkillDto)
            .toList();
    }

    private List<Skill> convertToSkillEntityList(List<com.reply.skillshub.openapi.model.Skill> apiSkills) {
        return apiSkills.stream()
            .map(this::convertToSkillEntity)
            .toList();
    }

    private Skill convertToSkillEntity(com.reply.skillshub.openapi.model.Skill  apiSkill) {
        Skill skillEntity = new Skill();
        skillEntity.setId(apiSkill.getId());
        skillEntity.setLabel(apiSkill.getLabel());
        return skillEntity;
    }

    private Optional<com.reply.skillshub.openapi.model.Industry> convertToIndustryDto(Optional<Industry> industry) {
        return industry.map(i -> new com.reply.skillshub.openapi.model.Industry()
            .id(i.getId())
            .label(i.getLabel()));
    }

    private List<Industry> convertToIndustries(Optional<com.reply.skillshub.openapi.model.Industry> indOptional) {
        return indOptional.stream()
            .map(this::convertToIndustry)
            .toList();
    }

    private Industry convertToIndustry(com.reply.skillshub.openapi.model.Industry industry) {
        Industry industryEntity = new Industry();
        industryEntity.setId(industry.getId());
        industryEntity.setLabel(industry.getLabel());
        return industryEntity;
    }
    
}
