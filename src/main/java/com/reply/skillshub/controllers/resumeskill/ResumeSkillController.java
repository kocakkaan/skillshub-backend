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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resumeSkillResumeSkillIdDelete'");
  }

  @Override
  public ResponseEntity<ResumeSkillDto> resumeSkillResumeSkillIdPut(String resumeSkillId, ResumeSkillDto resumeSkillDto) {
    return ResponseEntity.ok(resumeSkillControllerService.updateResumeSkillDto(resumeSkillId, resumeSkillDto));
  }
  
}
