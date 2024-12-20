package com.reply.skillshub.controllers.resumeskill;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.resumeskill.ResumeSkillService;
import com.reply.skillshub.data.skill.Skill;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeSkillControllerService {

  private final ResumeSkillService resumeSkillService;

  public void connectSubSkillToResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    Skill subSkill = new Skill();
    subSkill.setId(skillId);
    resumeSkill.getSkills().add(subSkill);
    resumeSkillService.save(resumeSkill);
  }

  public void connectParentSkillToResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    Skill parentSkill = new Skill();
    parentSkill.setId(skillId);
    resumeSkill.getParent();
    resumeSkill.setParent(List.of(parentSkill));
    resumeSkillService.save(resumeSkill);
  }

  public void disconnectSubSkillFromResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    resumeSkill.getSkills().removeIf(skill -> skill.getId().equals(skillId));
    resumeSkillService.save(resumeSkill);
  }

  public void disconnectParentSkillFromResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    resumeSkill.setParent(List.of());
    resumeSkillService.save(resumeSkill);
  }
  
}
