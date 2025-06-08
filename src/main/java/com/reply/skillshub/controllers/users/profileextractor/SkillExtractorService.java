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

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SkillExtractorService.class);

  private final SkillService skillService;

  public List<Skill> handleExtractedSkills(List<String> skills) {
    logger.info("Extracting skills from CV information");
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
    logger.info("Successfully Extracted skills");
    return extractedSkills;
  }
  
}
