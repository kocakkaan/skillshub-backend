package com.reply.skillshub.controllers.users.profileextractor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.experience.ExperienceService;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.occupation.OccupationService;
import com.reply.skillshub.services.CvInformation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExperienceExtractorService {

  private final ExperienceService experienceService;
  private final OccupationService occupationService;
  private static final Logger logger = LoggerFactory.getLogger(ExperienceExtractorService.class);

  public List<Experience> handleExtractedExperiences(List<CvInformation.Experience> experiences) {
    logger.info("Extracting experiences from CV information");
    List<Experience> extractedExperiences = new ArrayList<>();
    for (var experience : experiences) {
      Experience newExperience = new Experience();
      newExperience.setTitle(experience.getSummary());

      var optionalOccupation = occupationService.findByLabelIgnoreCase(experience.getTitle());
      if (optionalOccupation.isPresent()) {
        newExperience.setOccupation(optionalOccupation.get());
      } else {
        var newOccupation = new Occupation();
        newOccupation.setLabel(experience.getTitle());
        var savedOccupation = occupationService.save(newOccupation);
        newExperience.setOccupation(savedOccupation);
      }

      var descriptionLength = experience.getDescriptions().size();

      if (descriptionLength > 6) {
        descriptionLength = 6;
        logger.info("Description length is greater than 6, truncating to 6");
      }

      var description = experience.getDescriptions().subList(0, descriptionLength);
      newExperience.setDescriptions(description);

      newExperience.setStartDate(parseDate(experience.getStart_date()));
      newExperience.setEndDate(parseDate(experience.getEnd_date()));
      var savedExperience = experienceService.save(newExperience);
      extractedExperiences.add(savedExperience);
    }
    logger.info("Successfully extracted experiences");
    return extractedExperiences;
  }

  private LocalDate parseDate(String date) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
      var temporal = formatter.parse(date);
      var year = temporal.get(ChronoField.YEAR);
      var month = temporal.get(ChronoField.MONTH_OF_YEAR);
      var parsed_date = LocalDate.of(year, month, 1);
      return parsed_date;
    } catch (Exception e) {
      return null;
    }
  }
  
}
