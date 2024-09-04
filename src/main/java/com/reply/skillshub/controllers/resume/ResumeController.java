package com.reply.skillshub.controllers.resume;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ResumesApi;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.UpdateResumeRoleRequest;
import com.reply.skillshub.openapi.model.UpdateResumeTitleRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResumeController implements ResumesApi {

    private final ResumeControllerService resumeControllerService;

    @Override
    public ResponseEntity<Void> exportToPptx(String resumeId, @NotNull @Valid String language,
            @NotNull @Valid String company) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToPptx'");
    }

    @Override
    public ResponseEntity<Void> resumesResumeIdDelete(String resumeId) {
        resumeControllerService.deleteResumeById(resumeId);
        return ResponseEntity.status(204).build();
    }

    @Override
    public ResponseEntity<ResumeDto> resumesResumeIdExperiencesPatch(String resumeId,
            @Valid List<@Valid ResumeExperienceDto> resumeExperience) {
        return ResponseEntity.ok(resumeControllerService.updateExperiencesListForResume(resumeId, resumeExperience));
    }

    @Override
    public ResponseEntity<ResumeDto> resumesResumeIdGet(String resumeId) {
        return ResponseEntity.ok(resumeControllerService.findResumeById(resumeId));
    }

    @Override
    public ResponseEntity<ResumeDto> resumesResumeIdIndustriesPatch(String resumeId, @Valid List<IndustryDto> industries) {
        return ResponseEntity.ok(resumeControllerService.updateResumeIndustries(resumeId, industries));
    }

    @Override
    public ResponseEntity<ResumeDto> resumesResumeIdPut(String resumeId, @Valid ResumeDto resumeDto) {
        return ResponseEntity.ok(resumeControllerService.updateResume(resumeId, resumeDto));
    }

    @Override
    public ResponseEntity<ResumeDto> resumesResumeIdSkillsPut(String resumeId, @Valid List<@Valid ResumeSkillDto> resumeSkill) {
        return ResponseEntity.ok(resumeControllerService.updateResumeSkills(resumeId, resumeSkill));
    }

    @Override
    public ResponseEntity<ResumeDto> updateResumeRole(String resumeId, @Valid UpdateResumeRoleRequest updateResumeRoleRequest) {
        return ResponseEntity.ok(resumeControllerService.updateResumeRole(resumeId, updateResumeRoleRequest));
    }

    @Override
    public ResponseEntity<ResumeDto> updateResumeTitle(String resumeId, @Valid UpdateResumeTitleRequest updateResumeTitleRequest) {
        return ResponseEntity.ok(resumeControllerService.updateResumeTitle(resumeId, updateResumeTitleRequest));
    }

    @Override
    public ResponseEntity<List<ResumeDto>> userMeResumesGet() {
        return ResponseEntity.ok(resumeControllerService.findResumesForCurrentUser());
    }

    @Override
    public ResponseEntity<ResumeDto> userMeResumesPost(@Valid ResumeDto resumeDto) {
        return ResponseEntity.ok(resumeControllerService.createResumeForCurrentUser(resumeDto));
    }

    @Override
    public ResponseEntity<List<ResumeDto>> usersUserIdResumesGet(String userId) {
        return ResponseEntity.ok(resumeControllerService.findResumesForUser(userId));
    }

    @Override
    public ResponseEntity<ResumeDto> usersUserIdResumesPost(String userId, @Valid ResumeDto resumeDto) {
        return ResponseEntity.ok(resumeControllerService.createResumeForUser(userId, resumeDto));
    }
    
}
