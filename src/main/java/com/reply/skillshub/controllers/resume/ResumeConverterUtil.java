package com.reply.skillshub.controllers.resume;

import java.util.List;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.openapi.model.BaseResumeDto;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.ShortCvDto;
import com.reply.skillshub.openapi.model.SkillDto;

public class ResumeConverterUtil {

    public ResumeConverterUtil() {
    };

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
        skill.setSkills(dto.getChildren());
        skill.setParent(dto.getParent());
        return skill;
    }

    public static SkillDto convertSkillToDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skill.getId());
        skillDto.setLabel(skill.getLabel());
        return skillDto;
    }

    public static ResumeSkillDto convertSkillToDto(ResumeSkill resumeSkill) {
        var resumeSkillDto = new ResumeSkillDto();
        resumeSkillDto.setId(resumeSkill.getId());
        resumeSkillDto.setChildren(resumeSkill.getSkills());
        resumeSkillDto.setParent(resumeSkill.getParent());
        return resumeSkillDto;
    }

    public static BaseResumeDto convertBaseResumeToDto(UserWithResumes.Resume resume) {
        var resumeDto = new BaseResumeDto();
        resumeDto.setTitle(resume.getTitle());
        resumeDto.setId(resume.getId());
        return resumeDto;
    }

    public static ShortCvDto convertResumeToDto(ShortCv resume) {
        ShortCvDto resumeDto = new ShortCvDto();
        resumeDto.setBackground(resume.getBackground());
        resumeDto.setTitle(resume.getTitle());
        resumeDto.setId(resume.getId());
        resumeDto.setRole(resume.getRole());
        resumeDto.setSkills(resume.getSkills().stream().map(ResumeConverterUtil::convertSkillToDto).toList());
        resumeDto.setIndustries(resume.getIndustries());
        resumeDto.setExperiences(convertExperiences(resume.getExperiences()));
        return resumeDto;
    }

    private static List<ResumeExperienceDto> convertExperiences(List<ResumeExperience> experiences) {
        return experiences.stream().map(ResumeConverterUtil::convertResumeExperienceToDto).toList();
    }

    static ResumeExperienceDto convertResumeExperienceToDto(ResumeExperience experience) {
        ResumeExperienceDto dto = new ResumeExperienceDto();
        dto.setDescriptions(experience.getDescriptions());
        dto.setTechnologies(experience.getTechnologies());
        dto.setTitle(experience.getTitle());
        dto.setRole(experience.getRole());
        return dto;
    }

    public static ResumeExperience convertExperienDtoToEntity(ResumeExperienceDto dto) {
        ResumeExperience experience = new ResumeExperience();
        experience.setDescriptions(dto.getDescriptions());
        experience.setTitle(dto.getTitle());
        experience.setRole(dto.getRole());
        experience.setTechnologies(dto.getTechnologies());
        return experience;
    }

}
