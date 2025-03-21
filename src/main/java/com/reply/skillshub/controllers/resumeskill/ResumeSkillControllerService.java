package com.reply.skillshub.controllers.resumeskill;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.resumeskill.ResumeSkillService;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.skill.SkillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeSkillControllerService {

  private final ResumeSkillService resumeSkillService;
  private final SkillService skillService;

  public void connectSubSkillToResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    Skill skill = skillService.findById(skillId);
    resumeSkill.getSkills().add(skill.getLabel());
    resumeSkillService.save(resumeSkill);
  }

  public void connectParentSkillToResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    Skill skill = skillService.findById(skillId);
    resumeSkill.setParent(skill.getLabel());
    resumeSkillService.save(resumeSkill);
  }

  public void disconnectSubSkillFromResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    Skill skill = skillService.findById(skillId);
    resumeSkill.getSkills().removeIf(rSkill -> rSkill.equals(skill.getLabel()));
    resumeSkillService.save(resumeSkill);
  }

  public void disconnectParentSkillFromResumeSkill(String skillId, String resumeSkillId) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    resumeSkill.setParent(null);
    resumeSkillService.save(resumeSkill);
  }

}
