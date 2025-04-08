package com.reply.skillshub.controllers.experience;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import static org.instancio.Select.field;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.openapi.model.ExperienceDto;
import com.reply.skillshub.openapi.model.IndustryDto;

public class ExperienceControllerServiceUtilTest {

  @Test
  void testUpdateExperienceWithBaseExperience() {

    // Arrange
    Experience experience = Instancio.create(Experience.class);
    ExperienceDto experienceDto = Instancio.create(ExperienceDto.class);

    // Act
    Experience updatedExperience = ExperienceControllerServiceUtil.updateExperienceWithBaseExperience(experience,
        experienceDto);

    // Assert
    assertEquals(experienceDto.getTitle(), updatedExperience.getTitle());
    assertIterableEquals(experienceDto.getResponsibilities(), updatedExperience.getDescriptions());

    // compare occupation
    assertEquals(experienceDto.getOccupationalCategory().getId(), updatedExperience.getOccupation().getId());
    assertEquals(experienceDto.getOccupationalCategory().getLabel(), updatedExperience.getOccupation().getLabel());

    // compare skills
    assertEquals(experienceDto.getSkills().size(), updatedExperience.getSkills().size());
    for (int i = 0; i < experienceDto.getSkills().size(); i++) {
      assertEquals(experienceDto.getSkills().get(i).getId(), updatedExperience.getSkills().get(i).getId());
      assertEquals(experienceDto.getSkills().get(i).getLabel(), updatedExperience.getSkills().get(i).getLabel());
    }

    experienceDto.getIndustry()
        .ifPresentOrElse(
            (industry) -> assertEquals(industry.getId(), updatedExperience.getIndustries().get(0).getId()),
            () -> assertEquals(List.of(), updatedExperience.getIndustries()));

    // compare industries
    var dtoIndustry = experienceDto.getIndustry().get();
    assertEquals(dtoIndustry.getId(), updatedExperience.getIndustries().get(0).getId());
    assertEquals(dtoIndustry.getLabel(), updatedExperience.getIndustries().get(0).getLabel());

    experienceDto.getStartDate()
        .ifPresentOrElse(
            (date) -> assertEquals(date, updatedExperience.getStartDate()),
            () -> assertEquals(null, updatedExperience.getStartDate()));

    experienceDto.getEndDate()
        .ifPresentOrElse(
            (date) -> assertEquals(date, updatedExperience.getEndDate()),
            () -> assertEquals(null, updatedExperience.getEndDate()));

  }

  @ParameterizedTest
  @MethodSource("provideFalseIndustries")
  void testUpdateExperienceWithBaseExperienceWithWeirdIndustries(ExperienceDto experienceDto) {
    // Arrange
    Experience experience = Instancio.create(Experience.class);

    // Act
    Experience updatedExperience = ExperienceControllerServiceUtil.updateExperienceWithBaseExperience(experience,
        experienceDto);

    assertEquals(0, updatedExperience.getIndustries().size());

  }

  private static Stream<Arguments> provideFalseIndustries() {
    var startingPoint = Instancio.of(ExperienceDto.class);
    var noId = startingPoint.set(field(IndustryDto::getId), null).create();
    var emptyId = startingPoint.set(field(IndustryDto::getId), "").create();
    var emptyLabel = startingPoint.set(field(IndustryDto::getLabel), "").create();
    var noLabel = startingPoint.set(field(IndustryDto::getLabel), null).create();
    return Stream.of(
        Arguments.of(noId),
        Arguments.of(emptyId),
        Arguments.of(emptyLabel),
        Arguments.of(noLabel));
  }

}
