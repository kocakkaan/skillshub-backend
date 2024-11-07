package com.reply.skillshub.controllers.resume;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resume.ResumeService;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
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
        return resumeService.findByUserId(currentUser.getId()).stream().map(ResumeConverterUtil::convertResumeToDto).toList();
    }

    public List<ResumeDto> findResumesForUser(String userId) {
        return resumeService.findByUserId(userId).stream().map(ResumeConverterUtil::convertResumeToDto).toList();
    }

    public void deleteResumeById(String id ) {
        resumeService.deleteById(id);
    }

    public Resume findResumeEntityById(String id) {
        return resumeService.findById(id);
    }

    public ResumeDto findResumeById(String id) {
        Resume resume = resumeService.findById(id);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto createResumeForCurrentUser(ResumeDto resumeDto) {
        return createResumeDto(loadCurrentUser.loadSkillhubUserFromContext(), resumeDto);
    }

    public ResumeDto createResumeForUser(String userId, ResumeDto resumeDto) {
        return createResumeDto(userService.findById(userId), resumeDto);
    }

    public ResumeDto updateResume(String resumeId, ResumeDto resumeDto) {
        Resume resume = resumeService.findById(resumeId);
        return ResumeConverterUtil.convertResumeToDto(resumeService.save(updateResumeWithDto(resume, resumeDto)));
    }

    private Resume updateResumeWithDto(Resume resume, ResumeDto resumeDto) {
        resume.setBackground(resumeDto.getBackground());
        resume.setTitle(resumeDto.getTitle());
        // resume.setRole(resumeDto.getPositionField());
        resume.setIndustries(resumeDto.getIndustries().stream().map(ResumeConverterUtil::convertIndustryDtoToEntity).toList());
        resume.setSkills(resumeDto.getSkills().stream().map(ResumeConverterUtil::convertSkillDtoToEntity).toList());
        resume.setExperiences(resumeDto.getExperiences().stream().map((dto) -> ResumeConverterUtil.convertExperienDtoToEntity(experienceService.loadById(dto.getBasedOf()), dto)).toList());
        return resume;
    }


    private ResumeDto createResumeDto(User user, ResumeDto resumeDto) {
        Resume resume = new Resume();
        updateResumeWithDto(resume, resumeDto);
        resume.getUsers().add(user);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateExperiencesListForResume(String id, List<ResumeExperienceDto> experience) {
        Resume resume = resumeService.findById(id);
        List<ResumeExperience> experiences = experience.stream().map((dto) -> ResumeConverterUtil.convertExperienDtoToEntity(experienceService.loadById(dto.getId()), dto)).toList();
        resume.setExperiences(experiences);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeTitle(String id, UpdateResumeTitleRequest title) {
        Resume resume = resumeService.findById(id);
        resume.setTitle(title.getTitle());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeRole(String id, UpdateResumeRoleRequest role) {
        Resume resume = resumeService.findById(id);
        resume.setRole(role.getLabel());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeBackground(String id, String background) {
        Resume resume = resumeService.findById(id);
        resume.setBackground(background);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeSkills(String id, List<ResumeSkillDto> skills) {
        Resume resume = resumeService.findById(id);
        List<ResumeSkill> resumeSkills = skills.stream().map(ResumeConverterUtil::convertSkillDtoToEntity).toList();
        resume.setSkills(resumeSkills);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeIndustries(String id, List<IndustryDto> industries) {
        Resume resume = resumeService.findById(id);
        resume.setIndustries(industries.stream().map(ResumeConverterUtil::convertIndustryDtoToEntity).toList());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }


}
