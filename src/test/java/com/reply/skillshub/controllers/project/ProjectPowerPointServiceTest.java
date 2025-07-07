package com.reply.skillshub.controllers.project;

import static org.instancio.Select.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reply.skillshub.controllers.project.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.project.powerpoint.ProjectPowerPointService;
import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.data.project.reference.ProjectReference;

public class ProjectPowerPointServiceTest {

  private final ProjectPowerPointService service = new ProjectPowerPointService();

  @BeforeEach
  public void setUp() throws Exception {
    // Use reflection to set the projectPicturePath field
    Field field = ProjectPowerPointService.class.getDeclaredField("projectPicturePath");
    field.setAccessible(true); // Allow access to private field
    field.set(service, "path/to/pictures"); // Set the value

  }

  @Test
  public void testCreatePowerPointDtoWithNullProject() {
    // Act
    PowerPointInformation result = service.createPowerPointDto(null, "en");

    // Assert
    assertNotNull(result);
    assertNull(result.getProjectId());
    assertNull(result.getProjectTitle());
    assertEquals(0, result.getApproachTechnologies().size());
    assertEquals(0, result.getInitialSituation().size());
    assertEquals(0,result.getChallenges().size());
    assertNull(result.getValueAddedText0());
    assertNull(result.getValueAddedText1());
    assertNull(result.getValueAddedText2());
    assertNull(result.getProjectPictureLocation());
    assertNull(result.getLanguage());
  }

  @Test
  public void testCreatePowerPointDtoWithProject() {
    // Arrange
    var project = Instancio.create(Project.class);
    project.getReferences().get(0).setLanguage("en");
    PowerPointInformation result = service.createPowerPointDto(project, "en");

    compareProjects("en", project, result);
  }

  @Test
  public void testCreatePowerPointDtoWithProjectAndDifferentLanguage() {
    // Arrange
    var project = Instancio.create(Project.class);
    project.getReferences().get(0).setLanguage("de");
    PowerPointInformation result = service.createPowerPointDto(project, "de");
    compareProjects("de", project, result);
  }

  @Test
  public void testCreatePowerPointDtoWithProjectAndNullLanguage() {
    // Arrange
    var project = Instancio.create(Project.class);
    var deReference = Instancio.of(ProjectReference.class).set(field(ProjectReference::getApproachTechnologies), List.of()).create();
    deReference.setLanguage("de");
    var enReference = Instancio.create(ProjectReference.class);
    enReference.setLanguage("en");
    project.getReferences().add(deReference);
    project.getReferences().add(enReference);
    project.getReferences().get(0).setLanguage("de");
    PowerPointInformation result = service.createPowerPointDto(project, "de");

  }

  @Test
  public void testCreatePowerPointDtoWithProjectAndEmptyLanguage() {
    var information = Instancio.create(PowerPointInformation.class);
    information.setApproachTechnologies(List.of());
    information.setInitialSituation(List.of());
    information.setChallenges(List.of());
    service.createPowerPointFromTemplate(information);
  }


  private static void compareProjects(String language, Project project, PowerPointInformation info) {
    assertEquals(project.getFormattedProjectId(), info.getProjectId());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getTitle(), info.getProjectTitle());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getApproachTechnologies().size(),
        info.getApproachTechnologies().size());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getInitialSituation().size(),
        info.getInitialSituation().size());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getChallenges().size(),
        info.getChallenges().size());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getValueAddedText0(), info.getValueAddedText0());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getValueAddedText1(), info.getValueAddedText1());
    assertEquals(project.getReferenceByLanguage(language).orElseThrow().getValueAddedText2(), info.getValueAddedText2());
  }

}
