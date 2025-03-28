package com.reply.skillshub.controllers.resumeskill;

import org.springframework.stereotype.Service;

import com.reply.skillshub.controllers.resume.ResumeConverterUtil;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.resumeskill.ResumeSkillService;
import com.reply.skillshub.openapi.model.ResumeSkillDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeSkillControllerService {

  private final ResumeSkillService resumeSkillService;

  public ResumeSkillDto updateResumeSkillDto(String resumeSkillId, ResumeSkillDto resumeSkillDto) {
    ResumeSkill resumeSkill = resumeSkillService.findById(resumeSkillId);
    resumeSkill.setParent(resumeSkillDto.getParent());
    resumeSkill.setSkills(resumeSkillDto.getChildren());
    var savedResumeSkill = resumeSkillService.save(resumeSkill);
    return ResumeConverterUtil.convertSkillToDto(savedResumeSkill);
  }

}
