package com.reply.skillshub.resume;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import com.reply.skillshub.controllers.resume.ShortCvGeneratorUtils;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.services.GeneratedShortCv;
import com.reply.skillshub.services.GeneratedShortCv.CvExperience;

public class ShortCvGeneratorUtilsTest {

  @Test
  void testConvertToShortCv() {
    GeneratedShortCv generatedShortCv = Instancio.of(GeneratedShortCv.class)
        .supply(field(GeneratedShortCv.CvExperience::getDescriptions),
            () -> Instancio.ofList(GeneratedShortCv.CvExperience.class).size(5).create())
        .create();

    ShortCv shortCv = ShortCvGeneratorUtils.convertToShortCv(generatedShortCv);

    assertEquals(generatedShortCv.getProfessionalBackground(), shortCv.getBackground());
    if (generatedShortCv.getIndustries().size() > 5) {
      assertEquals(5, shortCv.getIndustries().size());
    } else {
      assertEquals(generatedShortCv.getIndustries().size(), shortCv.getIndustries().size());
    }
    assertEquals(generatedShortCv.getSkills().size(), shortCv.getSkills().size());
    assertEquals(generatedShortCv.getExperiences().size(), shortCv.getExperiences().size());

    for (int i = 0; i < generatedShortCv.getExperiences().size(); i++) {
      CvExperience generatedExperience = generatedShortCv.getExperiences().get(i);
      ResumeExperience shortCvExperience = shortCv.getExperiences().get(i);
      assertEquals(generatedExperience.getTitle(), shortCvExperience.getTitle());
      assertEquals(generatedExperience.getRole(), shortCvExperience.getRole());
      assertEquals(generatedExperience.getDescriptions().size(), shortCvExperience.getDescriptions().size());
      assertEquals(generatedExperience.getDescriptions(), shortCvExperience.getDescriptions());
    }
  }

  @Test
  void testConvertToShortCvMoreAsSixExperienceDescriptions() {
    GeneratedShortCv generatedShortCv = Instancio.of(GeneratedShortCv.class)
        .supply(field(GeneratedShortCv.CvExperience::getDescriptions),
            () -> Instancio.ofList(GeneratedShortCv.CvExperience.class).size(7).create())
        .create();

    ShortCv shortCv = ShortCvGeneratorUtils.convertToShortCv(generatedShortCv);

    assertEquals(generatedShortCv.getProfessionalBackground(), shortCv.getBackground());
    if (generatedShortCv.getIndustries().size() > 5) {
      assertEquals(5, shortCv.getIndustries().size());
    } else {
      assertEquals(generatedShortCv.getIndustries().size(), shortCv.getIndustries().size());
    }
    assertEquals(generatedShortCv.getSkills().size(), shortCv.getSkills().size());
    assertEquals(generatedShortCv.getExperiences().size(), shortCv.getExperiences().size());

    for (int i = 0; i < generatedShortCv.getExperiences().size(); i++) {
      CvExperience generatedExperience = generatedShortCv.getExperiences().get(i);
      ResumeExperience shortCvExperience = shortCv.getExperiences().get(i);
      assertEquals(generatedExperience.getTitle(), shortCvExperience.getTitle());
      assertEquals(generatedExperience.getRole(), shortCvExperience.getRole());
      assertEquals(6, shortCvExperience.getDescriptions().size());
      assertEquals(generatedExperience.getDescriptions().subList(0, 6), shortCvExperience.getDescriptions());
    }
  }

}
