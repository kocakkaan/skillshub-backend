package com.reply.skillshub.controllers.resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.controllers.resume.interfaces.UserWithShortCvs;
import com.reply.skillshub.controllers.resume.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.resume.powerpoint.PowerPointService;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.resume.ResumeService;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeexperience.ResumeExperienceService;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.BaseResumeDto;
import com.reply.skillshub.openapi.model.CreateInitialResumeDto;
import com.reply.skillshub.openapi.model.ExportRequest;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.ResumesResumeIdBackgroundPatchRequest;
import com.reply.skillshub.openapi.model.ShortCvDto;
import com.reply.skillshub.openapi.model.UpdateResumeRoleRequest;
import com.reply.skillshub.openapi.model.UpdateResumeTitleRequest;
import com.reply.skillshub.openapi.model.UserWithShortCvDtos;
import com.reply.skillshub.services.SkillsAgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeControllerService {

    private final ResumeService resumeService;
    private final ResumeExperienceService resumeExperienceService;
    private final ExperienceService experienceService;
    private final UserService userService;
    private final LoadCurrentUser loadCurrentUser;
    private final SkillsAgentService skillsAgentService;
    private final PowerPointService powerPointService;
    private static final Logger logger = LoggerFactory.getLogger(ResumeControllerService.class);

    public XMLSlideShow createPowerPointForMultipleShortCvs(ExportRequest exportRequest) {
        List<PowerPointInformation> pptInfoList = new ArrayList<>();
        for (var selection : exportRequest.getSelection()) {
            if (selection.getResumeId() == null || selection.getResumeId().isBlank()) {
                throw new ValidationException("Resume ID must not be null or empty");
            }
            if (selection.getUserId() == null || selection.getUserId().isBlank()) {
                throw new ValidationException("User ID must not be null or empty");
            }
            var resume = resumeService.findById(selection.getResumeId());
            var user = userService.findByResumeId(selection.getResumeId());
            var pptInfo = powerPointService.createPowerPointDto(user, resume, "en", "reply");

            pptInfoList.add(pptInfo);
        }

        return powerPointService.createSlideShowFromMultipleTemplates(pptInfoList);
    }

    public List<UserWithShortCvDtos> findResumesForUsers(List<String> users) {
        if (users == null || users.isEmpty()) {
            throw new ValidationException("Users must not be empty");
        }

        var userWithShortCvDtosList = users.stream().map(userId -> {
            var user = userService.findById(userId, UserWithShortCvs.class);
            var userWithShortCvDtos = new UserWithShortCvDtos();
            userWithShortCvDtos.setUserId(user.getId());

            var baseResumes = user.getResumes().stream()
                    .map(resume -> {
                        var baseResume = new BaseResumeDto(resume.getId(), resume.getTitle());
                        baseResume.setRole(Optional.ofNullable(resume.getRole()));
                        return baseResume;
                    }).toList();

            userWithShortCvDtos.setShortCvs(baseResumes);
            return userWithShortCvDtos;
        }).toList();

        return userWithShortCvDtosList;
    }

    public ShortCvDto autoGenerateShortCv(String userId, String requirements) {
        if (userService.existsById(userId) == false) {
            throw new UserNotFound();
        }

        if (requirements == null || requirements.isBlank()) {
            throw new ValidationException("Requirements must not be empty");
        }

        var optionalGeneratedShortCv = skillsAgentService.generateShortCv(userId, requirements);

        if (optionalGeneratedShortCv.isPresent()) {
            var generated = optionalGeneratedShortCv.get();
            var user = userService.findById(userId, UserWithResumesToSave.class);
            var resumeToSave = ShortCvGeneratorUtils.convertToShortCv(generated);
            resumeToSave.setTitle("Autogenerated Short CV");
            resumeToSave.setRole("Software Developer");
            var savedResume = resumeService.save(resumeToSave);
            user.getResumes().add(savedResume);
            userService.save(user);
            return ResumeConverterUtil.convertResumeToDto(savedResume);
        }

        logger.error("Failed to generate short CV with id {} and requirements {}", userId, requirements);

        return null;
    }

    public List<BaseResumeDto> findResumesForCurrentUser() {
        var currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        var user = userService.findById(currentUser.getId(), UserWithResumes.class);
        return user.getResumes().stream().map(ResumeConverterUtil::convertBaseResumeToDto).toList();
    }

    public List<BaseResumeDto> findResumesForUser(String userId) {
        var user = userService.findById(userId, UserWithResumes.class);
        return user.getResumes().stream().map(ResumeConverterUtil::convertBaseResumeToDto).toList();
    }

    public List<ResumeSkillDto> findResumeSkills(String id) {
        var resumeSkills = resumeService.findById(id, ResumeWithResumeSkills.class).getSkills();
        return resumeSkills.stream().map(ResumeConverterUtil::convertSkillToDto).toList();
    }

    public List<ResumeExperienceDto> findResumeExperiences(String id) {
        var resumeExperiences = resumeService.findById(id, ResumeWithResumeExperience.class).getExperiences();
        return resumeExperiences.stream().map(ResumeConverterUtil::convertResumeExperienceToDto).toList();
    }

    public List<String> findResumeIndustries(String id) {
        var resume = resumeService.findById(id, ResumeWithIndustries.class);
        return resume.getIndustries();
    }

    public ResumeExperienceDto findResumeExperienceById(String id) {
        return ResumeConverterUtil.convertResumeExperienceToDto(resumeExperienceService.findById(id));
    }

    public ResumeExperienceDto updateResumeExperience(String id, ResumeExperienceDto experience) {
        ResumeExperience resumeExperience = resumeExperienceService.findById(id);
        resumeExperience.setDescriptions(experience.getDescriptions());
        resumeExperienceService.save(resumeExperience);
        return ResumeConverterUtil.convertResumeExperienceToDto(resumeExperience);
    }

    public void deleteResumeById(String id) {
        resumeService.deleteById(id);
    }

    public void deleteResumeExperienceById(String id) {
        resumeExperienceService.deleteById(id);
    }

    public ShortCv findResumeEntityById(String id) {
        return resumeService.findById(id);
    }

    public ShortCvDto findResumeById(String id) {
        ShortCv resume = resumeService.findById(id);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ShortCvDto createResumeForCurrentUser(ShortCvDto resumeDto) {
        return createResume(userService.findById(loadCurrentUser.loadSkillhubUserFromContext().getId(),
                UserWithBaseResumesToSave.class), resumeDto);
    }

    public ShortCvDto createInitialResumeForUser(String userId, CreateInitialResumeDto createInitialResumeDto) {
        var userWithResumes = userService.findById(userId, UserWithBaseResumesToSave.class);
        return createInitialResumeDto(userWithResumes, createInitialResumeDto);
    }

    public ShortCvDto createResumeForUser(String userId, ShortCvDto resumeDto) {
        var userWithResumes = userService.findById(userId, UserWithBaseResumesToSave.class);
        return createResume(userWithResumes, resumeDto);
    }

    public ShortCvDto updateResume(String resumeId, ShortCvDto resumeDto) {
        ShortCv resume = resumeService.findById(resumeId);
        return ResumeConverterUtil.convertResumeToDto(resumeService.save(updateResumeWithDto(resume, resumeDto)));
    }

    private ShortCv updateResumeWithDto(ShortCv resume, ShortCvDto resumeDto) {
        resume.setBackground(resumeDto.getBackground());
        resume.setTitle(resumeDto.getTitle());
        // resume.setRole(resumeDto.getPositionField());
        resume.setIndustries(resumeDto.getIndustries());
        resume.setSkills(resumeDto.getSkills().stream().map(ResumeConverterUtil::convertSkillDtoToEntity).toList());
        resume.setExperiences(resumeDto.getExperiences().stream()
                .map((dto) -> ResumeConverterUtil.convertExperienDtoToEntity(dto)).toList());
        return resume;
    }

    private ShortCvDto createInitialResumeDto(UserWithBaseResumesToSave user,
            CreateInitialResumeDto createInitialResumeDto) {
        ShortCv resume = new ShortCv();
        if (createInitialResumeDto.getBaseResumeId().isPresent()) {
            ShortCv baseResume = resumeService.findById(createInitialResumeDto.getBaseResumeId().get());
            resume.setRole(baseResume.getRole());
            resume.setIndustries(baseResume.getIndustries());
            resume.setSkills(baseResume.getSkills());
            resume.setExperiences(baseResume.getExperiences());
        }
        resume.setTitle(createInitialResumeDto.getTitle());
        var savedResume = resumeService.save(resume);
        var resumeToAddToUser = new BaseResume();
        resumeToAddToUser.setId(savedResume.getId());
        resumeToAddToUser.setTitle(savedResume.getTitle());
        user.getResumes().add(resumeToAddToUser);
        userService.save(user);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    private ShortCvDto createResume(UserWithBaseResumesToSave user, ShortCvDto resumeDto) {
        ShortCv resume = new ShortCv();
        updateResumeWithDto(resume, resumeDto);
        var savedResume = resumeService.save(resume);
        var resumeToAddToUser = new BaseResume();
        resumeToAddToUser.setId(savedResume.getId());
        resumeToAddToUser.setTitle(savedResume.getTitle());
        user.getResumes().add(resumeToAddToUser);
        userService.save(user);
        return ResumeConverterUtil.convertResumeToDto(savedResume);
    }

    public ShortCvDto updateExperiencesListForResume(String id, List<ResumeExperienceDto> experience) {
        ShortCv resume = resumeService.findById(id);
        List<ResumeExperience> experiences = experience.stream()
                .map((dto) -> ResumeConverterUtil.convertExperienDtoToEntity(dto)).toList();
        resume.setExperiences(experiences);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ShortCvDto updateResumeTitle(String id, UpdateResumeTitleRequest title) {
        ShortCv resume = resumeService.findById(id);
        resume.setTitle(title.getTitle());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ShortCvDto updateResumeRole(String id, UpdateResumeRoleRequest role) {
        ShortCv resume = resumeService.findById(id);
        resume.setRole(role.getLabel());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ShortCvDto updateResumeBackground(String id, ResumesResumeIdBackgroundPatchRequest background) {
        ShortCv resume = resumeService.findById(id);
        resume.setBackground(background.getBackground());
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public ShortCvDto updateResumeBackground(String id, String background) {
        ShortCv resume = resumeService.findById(id);
        resume.setBackground(background);
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

    public List<ResumeSkillDto> updateResumeSkills(String id, List<ResumeSkillDto> skills) {
        ShortCv resume = resumeService.findById(id);
        List<ResumeSkill> resumeSkills = skills.stream().map(ResumeConverterUtil::convertSkillDtoToEntity).toList();
        resume.setSkills(resumeSkills);
        var savedResume = resumeService.save(resume);
        return savedResume.getSkills().stream().map(rSkill -> ResumeConverterUtil.convertSkillToDto(rSkill)).toList();
    }

    public List<String> updateResumeIndustries(String id, List<String> industries) {
        var resume = resumeService.findById(id, ResumeWithIndustries.class);
        resume.setIndustries(industries);
        var savedResume = resumeService.save(resume);
        return savedResume.getIndustries();
    }

    public ShortCvDto addExperiencesToResume(String id, List<String> ids) {
        var experiences = experienceService.findAllById(ids);
        var resume = resumeService.findById(id);
        experiences.forEach(experience -> {
            var resumeExperience = new ResumeExperience();
            resumeExperience.setDescriptions(experience.getDescriptions());
            resumeExperience.setRole(experience.getOccupation().getLabel());
            resumeExperience.setTitle(experience.getTitle());
            resume.getExperiences().add(resumeExperience);
        });
        resumeService.save(resume);
        return ResumeConverterUtil.convertResumeToDto(resume);
    }

}
