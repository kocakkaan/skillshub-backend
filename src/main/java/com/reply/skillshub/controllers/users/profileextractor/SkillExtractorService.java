package com.reply.skillshub.controllers.users.profileextractor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.skill.SkillService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SkillExtractorService {

  private final SkillService skillService;

  public List<Skill> handleExtractedSkills(List<String> skills) {
    List<Skill> extractedSkills = new ArrayList<>();
    for (String skill : skills) {
      var optionalSkill = skillService.findByLabelIgnoreCase(skill);
      if (optionalSkill.isEmpty()) {
        var newSkill = new Skill();
        newSkill.setLabel(skill);
        skillService.save(newSkill);
        extractedSkills.add(newSkill);
      } else {
        extractedSkills.add(optionalSkill.get());
      }
    }
    return extractedSkills;
  }
  
}
