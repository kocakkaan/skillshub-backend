package com.reply.skillshub.controllers.resumeskill;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ResumesSkillsApi;
import com.reply.skillshub.openapi.model.ResumeSkillDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResumeSkillController implements ResumesSkillsApi {

  private final ResumeSkillControllerService resumeSkillControllerService;

  @Override
  public ResponseEntity<Void> resumeSkillResumeSkillIdDelete(String resumeSkillId) {
    resumeSkillControllerService.deleteResumeSkill(resumeSkillId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ResumeSkillDto> resumeSkillResumeSkillIdPut(String resumeSkillId, ResumeSkillDto resumeSkillDto) {
    return ResponseEntity.ok(resumeSkillControllerService.updateResumeSkillDto(resumeSkillId, resumeSkillDto));
  }
  
}
