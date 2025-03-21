package com.reply.skillshub.controllers.experience;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeSkillNotFound;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.ExperienceDto;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.SkillDto;

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
        return convertExperienceToDto(potential);
    }

    public List<ExperienceDto> getExperiencesByUserId(String userId) {
        var user = userService.findById(userId);
        return user.getExperiences().stream().map(this::convertExperienceToDto).toList();
    }

    public ExperienceDto updateExperienceById(String experienceId, ExperienceDto ExperienceDto) {
        var experience = experienceService.findById(experienceId).orElseThrow(() -> new ResumeSkillNotFound());
        return convertExperienceToDto(
                experienceService.save(updateExperienceWithBaseExperience(experience, ExperienceDto)));
    }

    public ExperienceDto createExperienceForUser(String userId, ExperienceDto ExperienceDto) {
        var user = userService.findById(userId, UserWithExperiences.class);
        Experience experience = new Experience();
        var savedExperience = experienceService.save(updateExperienceWithBaseExperience(experience, ExperienceDto));
        user.getExperiences().add(savedExperience);
        userService.save(user);
        return convertExperienceToDto(savedExperience);
    }

    private Experience updateExperienceWithBaseExperience(Experience experience, ExperienceDto ExperienceDto) {
        experience.setTitle(ExperienceDto.getTitle());
        experience.setDescriptions(ExperienceDto.getResponsibilities());
        experience.setIndustries(convertToIndustries(ExperienceDto.getIndustry()));
        experience.setSkills(convertToSkillEntityList(ExperienceDto.getSkills()));
        experience.setOccupation(convertToOccupation(ExperienceDto.getOccupationalCategory()));
        ExperienceDto.getStartDate().ifPresent((date) -> experience.setStartDate(date));
        ExperienceDto.getEndDate().ifPresent((date) -> experience.setEndDate(date));
        return experience;
    }

    private Occupation convertToOccupation(OccupationalCategoryDto occupationalCategoryDto) {
        Occupation occupation = new Occupation();
        occupation.setId(occupationalCategoryDto.getId());
        occupation.setLabel(occupationalCategoryDto.getLabel());
        return occupation;
    }

    private ExperienceDto convertExperienceToDto(BaseExperience experience) {
        return new ExperienceDto()
                .id(experience.getId())
                .responsibilities(experience.getDescriptions())
                .title(experience.getTitle())
                .occupationalCategory(experience.getOccupation().stream().map(oc -> new OccupationalCategoryDto()
                        .id(oc.getId())
                        .label(oc.getLabel()))
                        .findFirst().orElse(null))
                .skills(experience.getSkills().stream().map(this::convertToSkillDto).toList());
    }

    private ExperienceDto convertExperienceToDto(Experience experience) {
        return new ExperienceDto()
                .id(experience.getId())
                .industry(convertToIndustryDto(experience.getIndustry()).orElse(null))
                .responsibilities(experience.getDescriptions())
                .title(experience.getTitle())
                .occupationalCategory(new OccupationalCategoryDto()
                        .id(experience.getOccupation().getId())
                        .label(experience.getOccupation().getLabel()))
                .skills(convertToSkillDtoList(experience.getSkills()));
    }

    private SkillDto convertToSkillDto(Skill skill) {
        return new SkillDto()
                .id(skill.getId())
                .label(skill.getLabel());
    }

    private SkillDto convertToSkillDto(BaseExperience.Skill skill) {
        return new SkillDto()
                .id(skill.getId())
                .label(skill.getLabel());
    }

    private List<SkillDto> convertToSkillDtoList(List<Skill> skills) {
        return skills.stream()
                .map(this::convertToSkillDto)
                .toList();
    }

    private List<Skill> convertToSkillEntityList(List<SkillDto> apiSkills) {
        return apiSkills.stream()
                .map(this::convertToSkillEntity)
                .toList();
    }

    private Skill convertToSkillEntity(SkillDto apiSkill) {
        Skill skillEntity = new Skill();
        skillEntity.setId(apiSkill.getId());
        skillEntity.setLabel(apiSkill.getLabel());
        return skillEntity;
    }

    private Optional<IndustryDto> convertToIndustryDto(Optional<Industry> industry) {
        return industry.map(i -> new IndustryDto()
                .id(i.getId())
                .label(i.getLabel()));
    }

    private List<Industry> convertToIndustries(Optional<IndustryDto> indOptional) {
        return indOptional.stream()
                .map(this::convertToIndustry)
                .toList();
    }

    private Industry convertToIndustry(IndustryDto industry) {
        Industry industryEntity = new Industry();
        industryEntity.setId(industry.getId());
        industryEntity.setLabel(industry.getLabel());
        return industryEntity;
    }

}
