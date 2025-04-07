package com.reply.skillshub.controllers.resume;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.services.GeneratedShortCv;
import com.reply.skillshub.services.GeneratedShortCv.CvExperience;

public class ShortCvGeneratorUtils {

  private static final Logger logger = LoggerFactory.getLogger(ResumeControllerService.class);

  public static ShortCv convertToShortCv(GeneratedShortCv generatedShortCv) {
    var shortCV = new ShortCv();
    shortCV.setBackground(generatedShortCv.getProfessionalBackground());
    shortCV.setSkills(convertMapToResumeSkill(generatedShortCv.getSkills()));
    shortCV.setExperiences(convertCvExperienceToResumeExperience(generatedShortCv.getExperiences()));
    shortCV.setIndustries(generatedShortCv.getIndustries());
    return shortCV;
  }

  private static List<ResumeExperience> convertCvExperienceToResumeExperience(List<CvExperience> experiences) {
    return experiences.stream().map((experience) -> {
      var resumeExperience = new ResumeExperience();
      var descriptionLength = experience.getDescriptions().size();
      if (descriptionLength > 6) {
        logger.warn("Experience Description length is greater than 6, it will be cuttoff");
        descriptionLength = 6;
      }
      resumeExperience.setDescriptions(experience.getDescriptions().subList(0, descriptionLength));
      resumeExperience.setTitle(experience.getTitle());
      resumeExperience.setRole(experience.getRole());
      return resumeExperience;
    }).toList();
  }

  private static List<ResumeSkill> convertMapToResumeSkill(HashMap<String, List<String>> skills) {
    return skills.entrySet().stream().map((entry) -> {
      var resumeSkill = new ResumeSkill();
      resumeSkill.setParent(entry.getKey());
      resumeSkill.setSkills(entry.getValue());
      return resumeSkill;
    }).toList();
  }

}
