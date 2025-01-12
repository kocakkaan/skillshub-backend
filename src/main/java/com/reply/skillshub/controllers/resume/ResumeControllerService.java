package com.reply.skillshub.controllers.resume;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.industry.IndustryService;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resume.ResumeService;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeexperience.ResumeExperienceService;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.resumeskill.ResumeSkillService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CreateInitialResumeDto;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.ResumesResumeIdBackgroundPatchRequest;
import com.reply.skillshub.openapi.model.UpdateResumeRoleRequest;
import com.reply.skillshub.openapi.model.UpdateResumeTitleRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeControllerService {

    private final ResumeService resumeService;
    private final ResumeSkillService resumeSkillService;
    private final IndustryService industryService;
    private final ResumeExperienceService resumeExperienceService;
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

    public List<ResumeSkillDto> findResumeSkills(String id) {
        return resumeSkillService.findAllByResumesId(id).stream().map(ResumeConverterUtil::convertSkillToDto).toList();
    }

    public List<ResumeExperienceDto> findResumeExperiences(String id) {
        return resumeExperienceService.findAllByResumesId(id).stream().map(ResumeConverterUtil::convertResumeExperienceToDto).toList();
    }

    public List<IndustryDto> findResumeIndustries(String id) {
        return industryService.findAllByResumeId(id).stream().map(ResumeConverterUtil::convertIndustryToDto).toList();
    }

    public ResumeExperienceDto findResumeExperienceById(String id) {
        return ResumeConverterUtil.convertResumeExperienceToDto(resumeExperienceService.findById(id));
    }

    public ResumeExperienceDto updateResumeExperience(String id, ResumeExperienceDto experience) {
        ResumeExperience resumeExperience = resumeExperienceService.findById(id);
        resumeExperience.setDescriptions(experience.getResponsibilities());
        resumeExperienceService.save(resumeExperience);
        return ResumeConverterUtil.convertResumeExperienceToDto(resumeExperience);
    }

    public void deleteResumeById(String id ) {
        resumeService.deleteById(id);
    }

    public void deleteResumeExperienceById(String id) {
        resumeExperienceService.deleteById(id);
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

    public ResumeDto createInitialResumeForUser(String userId, CreateInitialResumeDto createInitialResumeDto) {
        return createInitialResumeDto(userService.findById(userId), createInitialResumeDto);
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

    private ResumeDto createInitialResumeDto(User user, CreateInitialResumeDto createInitialResumeDto) {
        Resume resume = new Resume();
        if (createInitialResumeDto.getBaseResumeId().isPresent()) {
            Resume baseResume = resumeService.findById(createInitialResumeDto.getBaseResumeId().get());
            resume.setRole(baseResume.getRole());
            resume.setIndustries(baseResume.getIndustries());
            resume.setSkills(baseResume.getSkills());
            resume.setExperiences(baseResume.getExperiences());
        }
        resume.getUsers().add(user);
        resume.setTitle(createInitialResumeDto.getTitle());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
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
        var occupation = convertToRoleToOccupation(role);
        if (occupation != null) {
            resume.setRole(List.of(occupation));
        }
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ResumeDto updateResumeBackground(String id, ResumesResumeIdBackgroundPatchRequest background) {
        Resume resume = resumeService.findById(id);
        resume.setBackground(background.getBackground());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    private Occupation convertToRoleToOccupation(UpdateResumeRoleRequest role) {
        Occupation occupation = new Occupation();
        occupation.setLabel(role.getLabel());
        occupation.setId(role.getId());
        return occupation;
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

    public ResumeDto addExperiencesToResume(String id, List<String> ids) {
        var experiences = experienceService.findAllById(ids);
        var resume = resumeService.findById(id);
        experiences.forEach(experience -> {
            var resumeExperience = new ResumeExperience();
            resumeExperience.getBasedOfExperience().add(experience);
            resumeExperience.setDescriptions(experience.getDescriptions());
            resume.getExperiences().add(resumeExperience);
        });
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }


}
