package com.reply.skillshub.controllers.resume;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resume.ResumeService;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.UpdateResumeRoleRequest;
import com.reply.skillshub.openapi.model.UpdateResumeTitleRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeControllerService {

    private final ResumeService resumeService;
    private final ExperienceService experienceService;
    private final UserService userService;
    private final LoadCurrentUser loadCurrentUser;

    public List<ResumeDto> findResumesForCurrentUser() {
        User currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        return resumeService.findByUserId(currentUser.getId()).stream().map(this::convertResumeToDto).toList();
    }

    public List<ResumeDto> findResumesForUser(String userId) {
        return resumeService.findByUserId(userId).stream().map(this::convertResumeToDto).toList();
    }

    public void deleteResumeById(String id ) {
        resumeService.deleteById(id);
    }

    public ResumeDto findResumeById(String id) {
        Resume resume = resumeService.findById(id);
        return convertResumeToDto(resume);
    }

    public ResumeDto createResumeForCurrentUser(ResumeDto resumeDto) {
        return createResumeDto(loadCurrentUser.loadSkillhubUserFromContext(), resumeDto);
    }

    public ResumeDto createResumeForUser(String userId, ResumeDto resumeDto) {
        return createResumeDto(userService.findById(userId), resumeDto);
    }

    public ResumeDto updateResume(String resumeId, ResumeDto resumeDto) {
        Resume resume = resumeService.findById(resumeId);
        return convertResumeToDto(resumeService.save(updateResumeWithDto(resume, resumeDto)));
    }

    private Resume updateResumeWithDto(Resume resume, ResumeDto resumeDto) {
        resume.setBackground(resumeDto.getBackground());
        resume.setTitle(resumeDto.getTitle());
        // resume.setRole(resumeDto.getPositionField());
        resume.setIndustries(resumeDto.getIndustries().stream().map(this::convertIndustryDtoToEntity).toList());
        resume.setSkills(resumeDto.getSkills().stream().map(this::convertSkillDtoToEntity).toList());
        resume.setExperiences(resumeDto.getExperiences().stream().map(this::convertExperienDtoToEntity).toList());
        return resume;
    }


    private ResumeDto createResumeDto(User user, ResumeDto resumeDto) {
        Resume resume = new Resume();
        updateResumeWithDto(resume, resumeDto);
        resume.getUsers().add(user);
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateExperiencesListForResume(String id, List<ResumeExperienceDto> experience) {
        Resume resume = resumeService.findById(id);
        List<ResumeExperience> experiences = experience.stream().map(this::convertExperienDtoToEntity).toList();
        resume.setExperiences(experiences);
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateResumeTitle(String id, UpdateResumeTitleRequest title) {
        Resume resume = resumeService.findById(id);
        resume.setTitle(title.getTitle());
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateResumeRole(String id, UpdateResumeRoleRequest role) {
        Resume resume = resumeService.findById(id);
        resume.setRole(role.getLabel());
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateResumeBackground(String id, String background) {
        Resume resume = resumeService.findById(id);
        resume.setBackground(background);
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateResumeSkills(String id, List<ResumeSkillDto> skills) {
        Resume resume = resumeService.findById(id);
        List<ResumeSkill> resumeSkills = skills.stream().map(this::convertSkillDtoToEntity).toList();
        resume.setSkills(resumeSkills);
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    public ResumeDto updateResumeIndustries(String id, List<IndustryDto> industries) {
        Resume resume = resumeService.findById(id);
        resume.setIndustries(industries.stream().map(this::convertIndustryDtoToEntity).toList());
        resumeService.save(resume);
        return convertResumeToDto(resume);
    }

    private IndustryDto convertIndustryToDto(Industry industry) {
        IndustryDto industryDto = new IndustryDto();
        industryDto.setId(industry.getId());
        industryDto.setLabel(industry.getLabel());
        return industryDto;
    }

    private Industry convertIndustryDtoToEntity(IndustryDto dto) {
        Industry industry = new Industry();
        industry.setId(dto.getId());
        industry.setLabel(dto.getLabel());
        return industry;
    }

    private ResumeSkill convertSkillDtoToEntity(ResumeSkillDto dto) {
        ResumeSkill skill = new ResumeSkill();
        skill.setId(dto.getId());
        skill.getParent().add(convertSkillDtoToEntity(dto.getParentSkill()));
        skill.getSkills().addAll(convertSkillsDtoToEntity(dto.getRelatedEssentialSkills()));
        return skill;
    }

    private SkillDto convertSkillToDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skill.getId());
        skillDto.setLabel(skill.getLabel());
        return skillDto;
    }

    private ResumeSkillDto convertSkillToDto(ResumeSkill skill) {
        ResumeSkillDto skillDto = new ResumeSkillDto();
        skillDto.setId(skill.getId());
        skillDto.setParentSkill(convertSkillToDto(skill.getParent().get(0)));
        skillDto.setRelatedEssentialSkills(convertSkillsToDto(skill.getSkills()));
        return skillDto;
    }

    private List<SkillDto> convertSkillsToDto(List<Skill> dtos) {
        return dtos.stream().map(this::convertSkillToDto).toList();
    }

    private List<Skill> convertSkillsDtoToEntity(List<SkillDto> dtos) {
        return dtos.stream().map(this::convertSkillDtoToEntity).toList();
    }

    private Skill convertSkillDtoToEntity(SkillDto dto) {
        Skill skill = new Skill();
        skill.setId(dto.getId());
        skill.setLabel(dto.getLabel());
        return skill;
    }

    private ResumeDto convertResumeToDto(Resume resume) {
        ResumeDto resumeDto = new ResumeDto();
        resume.setBackground(resume.getBackground());
        resume.setTitle(resume.getTitle());
        return resumeDto;
    }

    private ResumeExperience convertExperienDtoToEntity(ResumeExperienceDto dto) {
        Experience basedOf = experienceService.loadById(dto.getBasedOf());
        ResumeExperience experience = new ResumeExperience();
        
        experience.setId(dto.getId());
        experience.setDescriptions(dto.getResponsibilities());
        experience.getBasedOfExperience().add(basedOf);
        return experience;
    }
}
