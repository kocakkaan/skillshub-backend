package com.reply.skillshub.controllers.resume;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.controllers.resume.powerpoint.PowerPointService;
import com.reply.skillshub.openapi.api.ResumesApi;
import com.reply.skillshub.openapi.model.CreateInitialResumeDto;
import com.reply.skillshub.openapi.model.IndustryDto;
import com.reply.skillshub.openapi.model.ResumeDto;
import com.reply.skillshub.openapi.model.ResumeExperienceDto;
import com.reply.skillshub.openapi.model.ResumeSkillDto;
import com.reply.skillshub.openapi.model.ResumesResumeIdBackgroundPatchRequest;
import com.reply.skillshub.openapi.model.UpdateResumeRoleRequest;
import com.reply.skillshub.openapi.model.UpdateResumeTitleRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResumeController implements ResumesApi {

  private final ResumeControllerService resumeControllerService;
  private final PowerPointService powerPointService;

  @Override
  public ResponseEntity<Void> exportToPptx(String resumeId, String language, String company) {
    var resume = resumeControllerService.findResumeEntityById(resumeId);
    var pptDto = powerPointService.createPowerPointDto(resume, language, company);
    var ppt = powerPointService.createPowerPointFromTemplate(pptDto);
    var test = new ByteArrayOutputStream();
    try {
      ppt.write(test);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // powerPointService.createPowerPoint();
    // TODO Auto-generated method stub
    // ResponseEntity.
    return ResponseEntity.status(200).build();
  }

  @Override
  public ResponseEntity<Void> resumesResumeIdDelete(String resumeId) {
    resumeControllerService.deleteResumeById(resumeId);
    return ResponseEntity.status(204).build();
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdExperiencesPatch(String resumeId,
      List<ResumeExperienceDto> resumeExperience) {
    return ResponseEntity.ok(resumeControllerService.updateExperiencesListForResume(resumeId, resumeExperience));
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdGet(String resumeId) {
    return ResponseEntity.ok(resumeControllerService.findResumeById(resumeId));
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdIndustriesPatch(String resumeId, List<IndustryDto> industries) {
    return ResponseEntity.ok(resumeControllerService.updateResumeIndustries(resumeId, industries));
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdPut(String resumeId, ResumeDto resumeDto) {
    return ResponseEntity.ok(resumeControllerService.updateResume(resumeId, resumeDto));
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdSkillsPut(String resumeId, List<ResumeSkillDto> resumeSkill) {
    return ResponseEntity.ok(resumeControllerService.updateResumeSkills(resumeId, resumeSkill));
  }

  @Override
  public ResponseEntity<ResumeDto> updateResumeRole(String resumeId,
      UpdateResumeRoleRequest updateResumeRoleRequest) {
    return ResponseEntity.ok(resumeControllerService.updateResumeRole(resumeId, updateResumeRoleRequest));
  }

  @Override
  public ResponseEntity<ResumeDto> updateResumeTitle(String resumeId,
      UpdateResumeTitleRequest updateResumeTitleRequest) {
    return ResponseEntity.ok(resumeControllerService.updateResumeTitle(resumeId, updateResumeTitleRequest));
  }

  @Override
  public ResponseEntity<List<ResumeDto>> userMeResumesGet() {
    return ResponseEntity.ok(resumeControllerService.findResumesForCurrentUser());
  }

  @Override
  public ResponseEntity<ResumeDto> userMeResumesPost(ResumeDto resumeDto) {
    return ResponseEntity.ok(resumeControllerService.createResumeForCurrentUser(resumeDto));
  }

  @Override
  public ResponseEntity<List<ResumeDto>> usersUserIdResumesGet(String userId) {
    return ResponseEntity.ok(resumeControllerService.findResumesForUser(userId));
  }

  @Override
  public ResponseEntity<ResumeDto> usersUserIdResumesPost(String userId,
      CreateInitialResumeDto createInitialResumeDto) {
    return ResponseEntity.ok(resumeControllerService.createInitialResumeForUser(userId, createInitialResumeDto));
  }

  @Override
  public ResponseEntity<Resource> resumesResumeIdExportToImgPost(String resumeId, String language, String company) {
    var resume = resumeControllerService.findResumeEntityById(resumeId);
    var pptDto = powerPointService.createPowerPointDto(resume, language, company);
    var ppt = powerPointService.createPowerPointFromTemplate(pptDto);
    var output = powerPointService.getFirstSlideAsImage(ppt);
    return ResponseEntity.status(200).body(output);
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdBackgroundPatch(String resumeId,
      ResumesResumeIdBackgroundPatchRequest resumesResumeIdBackgroundPatchRequest) {
    return ResponseEntity
        .ok(resumeControllerService.updateResumeBackground(resumeId, resumesResumeIdBackgroundPatchRequest));
  }

  @Override
  public ResponseEntity<ResumeDto> resumesResumeIdExperiencesPut(String resumeId, List<String> ids) {
    return ResponseEntity.ok(resumeControllerService.addExperiencesToResume(resumeId, ids));
  }

  @Override
  public ResponseEntity<Void> resumesResumeIdExperienceExperienceIdDelete(String resumeId, String experienceId) {
    resumeControllerService.deleteResumeExperienceById(experienceId);
    return ResponseEntity.status(204).build();
  }

  @Override
  public ResponseEntity<List<ResumeExperienceDto>> resumesResumeIdExperiencesGet(String resumeId) {
    return ResponseEntity.ok(resumeControllerService.findResumeExperiences(resumeId));
  }

  @Override
  public ResponseEntity<List<IndustryDto>> resumesResumeIdIndustriesGet(String resumeId) {
    return ResponseEntity.ok(resumeControllerService.findResumeIndustries(resumeId));
  }

  @Override
  public ResponseEntity<List<ResumeSkillDto>> resumesResumeIdSkillsGet(String resumeId) {
    return ResponseEntity.ok(resumeControllerService.findResumeSkills(resumeId));
  }

  @Override
  public ResponseEntity<ResumeExperienceDto> resumesResumeIdExperienceExperienceIdGet(String resumeId, String experienceId) {
    return ResponseEntity.ok(resumeControllerService.findResumeExperienceById(experienceId));
  }

  @Override
  public ResponseEntity<ResumeExperienceDto> resumesResumeIdExperienceExperienceIdPut(String resumeId, String experienceId,
      ResumeExperienceDto resumeExperienceDto) {
    return ResponseEntity.ok(resumeControllerService.updateResumeExperience(experienceId, resumeExperienceDto));
  }

}
