package com.reply.skillshub.controllers.experience;

import java.util.List;
import java.util.Optional;

import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.openapi.model.ExperienceDto;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.SkillDto;

public class ExperienceControllerServiceUtil {

    public static Experience updateExperienceWithBaseExperience(Experience experience, ExperienceDto ExperienceDto) {
        experience.setTitle(ExperienceDto.getTitle());
        experience.setDescriptions(ExperienceDto.getResponsibilities());
        experience.setIndustries(convertToIndustries(ExperienceDto.getIndustry()));
        experience.setSkills(convertToSkillEntityList(ExperienceDto.getSkills()));
        experience.setOccupation(convertToOccupation(ExperienceDto.getOccupationalCategory()));
        ExperienceDto.getStartDate().ifPresent((date) -> experience.setStartDate(date));
        ExperienceDto.getEndDate().ifPresent((date) -> experience.setEndDate(date));
        return experience;
    }

    private static Occupation convertToOccupation(OccupationalCategoryDto occupationalCategoryDto) {
        Occupation occupation = new Occupation();
        occupation.setId(occupationalCategoryDto.getId());
        occupation.setLabel(occupationalCategoryDto.getLabel());
        return occupation;
    }

    public static ExperienceDto convertExperienceToDto(BaseExperience experience) {
        return new ExperienceDto()
                .id(experience.getId())
                .responsibilities(experience.getDescriptions())
                .title(experience.getTitle())
                .occupationalCategory(experience.getOccupation().stream().map(oc -> new OccupationalCategoryDto()
                        .id(oc.getId())
                        .label(oc.getLabel()))
                        .findFirst().orElse(null))
                .skills(experience.getSkills().stream().map(ExperienceControllerServiceUtil::convertToSkillDto)
                        .toList());
    }

    public static ExperienceDto convertExperienceToDto(Experience experience) {
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

    private static SkillDto convertToSkillDto(Skill skill) {
        return new SkillDto()
                .id(skill.getId())
                .label(skill.getLabel());
    }

    private static SkillDto convertToSkillDto(BaseExperience.Skill skill) {
        return new SkillDto()
                .id(skill.getId())
                .label(skill.getLabel());
    }

    private static List<SkillDto> convertToSkillDtoList(List<Skill> skills) {
        return skills.stream()
                .map(ExperienceControllerServiceUtil::convertToSkillDto)
                .toList();
    }

    private static List<Skill> convertToSkillEntityList(List<SkillDto> apiSkills) {
        return apiSkills.stream()
                .map(ExperienceControllerServiceUtil::convertToSkillEntity)
                .toList();
    }

    private static Skill convertToSkillEntity(SkillDto apiSkill) {
        Skill skillEntity = new Skill();
        skillEntity.setId(apiSkill.getId());
        skillEntity.setLabel(apiSkill.getLabel());
        return skillEntity;
    }

    private static Optional<IndustryDto> convertToIndustryDto(Optional<Industry> industry) {
        return industry.map(i -> new IndustryDto()
                .id(i.getId())
                .label(i.getLabel()));
    }

    private static List<Industry> convertToIndustries(Optional<IndustryDto> indOptional) {
        if (indOptional.isEmpty()) {
            return List.of();
        }
        if (indOptional.get().getId() == null || indOptional.get().getId().isEmpty()) {
            return List.of();
        }
        if (indOptional.get().getLabel() == null || indOptional.get().getLabel().isEmpty()) {
            return List.of();
        }
        return indOptional.stream()
                .map(ExperienceControllerServiceUtil::convertToIndustry)
                .toList();
    }

    private static Industry convertToIndustry(IndustryDto industry) {
        Industry industryEntity = new Industry();
        industryEntity.setId(industry.getId());
        industryEntity.setLabel(industry.getLabel());
        return industryEntity;
    }

}
