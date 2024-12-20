package com.reply.skillshub.controllers.resumeskill;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ResumesSkillsApi;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResumeSkillController implements ResumesSkillsApi {

  private final ResumeSkillControllerService resumeSkillControllerService;

  @Override
  public ResponseEntity<Void> resumeSkillResumeSkillIdParentSkillsSkillIdDelete(String skillId, String resumeSkillId) {
    resumeSkillControllerService.disconnectParentSkillFromResumeSkill(skillId, resumeSkillId);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Void> resumeSkillResumeSkillIdParentSkillsSkillIdPost(String skillId, String resumeSkillId) {
    resumeSkillControllerService.connectParentSkillToResumeSkill(skillId, resumeSkillId);
    return ResponseEntity.status(200).build();

  }

  @Override
  public ResponseEntity<Void> resumeSkillResumeSkillIdRelatedSkillsSkillIdDelete(String skillId, String resumeSkillId) {
    resumeSkillControllerService.disconnectSubSkillFromResumeSkill(skillId, resumeSkillId);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Void> resumeSkillResumeSkillIdRelatedSkillsSkillIdPost(String skillId, String resumeSkillId) {
    resumeSkillControllerService.connectSubSkillToResumeSkill(skillId, resumeSkillId);
    return ResponseEntity.status(200).build();
  }
  
}
