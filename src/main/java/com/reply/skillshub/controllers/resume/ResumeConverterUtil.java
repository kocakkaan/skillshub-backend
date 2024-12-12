package com.reply.skillshub.controllers.resume;

import java.util.List;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeNotFound;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.SkillDto;

public class ResumeConverterUtil {

    public ResumeConverterUtil() {};

    public static IndustryDto convertIndustryToDto(Industry industry) {
        IndustryDto industryDto = new IndustryDto();
        industryDto.setId(industry.getId());
        industryDto.setLabel(industry.getLabel());
        return industryDto;
    }

    public static Industry convertIndustryDtoToEntity(IndustryDto dto) {
        Industry industry = new Industry();
        industry.setId(dto.getId());
        industry.setLabel(dto.getLabel());
        return industry;
    }

    public static ResumeSkill convertSkillDtoToEntity(ResumeSkillDto dto) {
        ResumeSkill skill = new ResumeSkill();
        skill.setId(dto.getId());
        skill.setParent(List.of(convertSkillDtoToEntity(dto.getParentSkill())));
        skill.getSkills().addAll(convertSkillsDtoToEntity(dto.getRelatedEssentialSkills()));
        return skill;
    }

    private static SkillDto convertSkillToDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skill.getId());
        skillDto.setLabel(skill.getLabel());
        return skillDto;
    }

    private static ResumeSkillDto convertSkillToDto(ResumeSkill skill) {
        ResumeSkillDto skillDto = new ResumeSkillDto();
        skillDto.setId(skill.getId());
        skillDto.setParentSkill(convertSkillToDto(skill.getParent()));
        skillDto.setRelatedEssentialSkills(convertSkillsToDto(skill.getSkills()));
        return skillDto;
    }

    private static List<SkillDto> convertSkillsToDto(List<Skill> dtos) {
        return dtos.stream().map(ResumeConverterUtil::convertSkillToDto).toList();
    }

    private static List<Skill> convertSkillsDtoToEntity(List<SkillDto> dtos) {
        return dtos.stream().map(ResumeConverterUtil::convertSkillDtoToEntity).toList();
    }

    private static Skill convertSkillDtoToEntity(SkillDto dto) {
        Skill skill = new Skill();
        skill.setId(dto.getId());
        skill.setLabel(dto.getLabel());
        return skill;
    }

    public static ResumeDto convertResumeToDto(Resume resume) {
        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setBackground(resume.getBackground());
        resumeDto.setTitle(resume.getTitle());
        resumeDto.setExperiences(convertExperiences(resume.getExperiences()));
        return resumeDto;
    }

    private static List<ResumeExperienceDto> convertExperiences(List<ResumeExperience> experiences) {
        return experiences.stream().map(ResumeConverterUtil::convertResumeExperienceToDto).toList();
    }

    static ResumeExperienceDto convertResumeExperienceToDto(ResumeExperience experience) {
        ResumeExperienceDto dto = new ResumeExperienceDto();
        Experience basedOf = experience.getBasedOf().orElseThrow(() -> new ResumeNotFound("Experience needs to exist here."));
        dto.setBasedOf(basedOf.getId());
        dto.setResponsibilities(experience.getDescriptions());
        dto.setSkills(convertSkillsToDto(basedOf.getSkills()));
        return dto;
    }

    public static ResumeExperience convertExperienDtoToEntity(Experience basedOf, ResumeExperienceDto dto) {
        ResumeExperience experience = new ResumeExperience();
        
        experience.setId(dto.getId());
        experience.setDescriptions(dto.getResponsibilities());
        experience.getBasedOfExperience().add(basedOf);
        return experience;
    }
    
}
