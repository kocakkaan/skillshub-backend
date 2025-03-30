package com.reply.skillshub.controllers.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ShortCvDto;

public class ResumeConverterUtilTest {

    @Test
    void testConvertIndustryToDto() {
        Industry industry = new Industry();
        industry.setId("1L");
        industry.setLabel("IT");

        IndustryDto industryDto = ResumeConverterUtil.convertIndustryToDto(industry);

        assertNotNull(industryDto);
        assertEquals(industry.getId(), industryDto.getId());
        assertEquals(industry.getLabel(), industryDto.getLabel());
    }

    @Test
    void testConvertIndustryDtoToEntity() {
        IndustryDto industryDto = new IndustryDto();
        industryDto.setId("1L");
        industryDto.setLabel("IT");

        Industry industry = ResumeConverterUtil.convertIndustryDtoToEntity(industryDto);

        assertNotNull(industry);
        assertEquals(industryDto.getId(), industry.getId());
        assertEquals(industryDto.getLabel(), industry.getLabel());
    }

    // Add more test methods for other conversion methods

    @Test
    void testConvertResumeToDto() {
        ShortCv resume = Instancio.create(ShortCv.class);
        resume.setBackground("Background");
        resume.setTitle("Title");

        ShortCvDto resumeDto = ResumeConverterUtil.convertResumeToDto(resume);

        assertNotNull(resumeDto);
        assertEquals(resume.getBackground(), resumeDto.getBackground());
        assertEquals(resume.getTitle(), resumeDto.getTitle());
        assertEquals(resume.getExperiences().size(), resumeDto.getExperiences().size());
        assertEquals(resume.getIndustries().size(), resumeDto.getIndustries().size());
        assertEquals(resume.getSkills().size(), resumeDto.getSkills().size());

    }

    // @Test
    // void testConvertExperienceDtoToEntity() {
    // Experience basedOf = new Experience();
    // basedOf.setId("1L");

    // ResumeExperienceDto experienceDto = new ResumeExperienceDto();
    // experienceDto.setId("1L");
    // experienceDto.setResponsibilities(List.of("Responsibilities"));

    // ResumeExperience experience =
    // ResumeConverterUtil.convertExperienDtoToEntity(basedOf, experienceDto);

    // assertNotNull(experience);
    // assertEquals(experienceDto.getId(), experience.getId());
    // assertEquals(experienceDto.getResponsibilities(),
    // experience.getDescriptions());
    // assertEquals(basedOf, experience.getBasedOfExperience().get(0));
    // }

    // @Test
    // void testConvertSkillDtoToEntity() {
    // ResumeSkillDto skillDto = new ResumeSkillDto();
    // skillDto.setId("1L");

    // SkillDto parentSkillDto = new SkillDto();
    // parentSkillDto.setId("2L");
    // parentSkillDto.setLabel("Parent Skill");
    // skillDto.setParentSkill(Optional.of(parentSkillDto));

    // List<SkillDto> relatedSkillsDto = new ArrayList<>();
    // SkillDto relatedSkillDto1 = new SkillDto();
    // relatedSkillDto1.setId("3L");
    // relatedSkillDto1.setLabel("Related Skill 1");
    // relatedSkillsDto.add(relatedSkillDto1);
    // SkillDto relatedSkillDto2 = new SkillDto();
    // relatedSkillDto2.setId("4L");
    // relatedSkillDto2.setLabel("Related Skill 2");
    // relatedSkillsDto.add(relatedSkillDto2);
    // skillDto.setRelatedEssentialSkills(relatedSkillsDto);

    // ResumeSkill skill = ResumeConverterUtil.convertSkillDtoToEntity(skillDto);

    // assertNotNull(skill);
    // assertEquals(skillDto.getId(), skill.getId());
    // String parentId = null;
    // if (skill.getParent().isPresent()) {
    // parentId = skill.getParent().get().getId();
    // }
    // assertEquals(parentSkillDto.getId(), parentId);
    // assertEquals(relatedSkillsDto.size(), skill.getSkills().size());
    // assertEquals(relatedSkillsDto.get(0).getId(),
    // skill.getSkills().get(0).getId());
    // assertEquals(relatedSkillsDto.get(1).getId(),
    // skill.getSkills().get(1).getId());
    // }
}