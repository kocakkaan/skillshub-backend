package com.reply.skillshub.controllers.project;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.openapi.model.ProjectDto;

public class ProjectControllerServiceUtilTest {

    @Test
    public void testConvertToProjectDto() {
        var project = Instancio.create(Project.class);
        var projectDto = ProjectControllerServiceUtil.convertToProjectDto(project);
        
        // Assertions to verify the conversion
        Assertions.assertEquals(project.getId(), projectDto.getId());
        Assertions.assertEquals(project.getTitle(), projectDto.getTitle());
        Assertions.assertIterableEquals(project.getApproachTechnologies(), projectDto.getApproach());
        Assertions.assertIterableEquals(project.getInitialSituation(), projectDto.getInitialSituation());
        Assertions.assertIterableEquals(project.getChallenges(), projectDto.getChallenges());
        Assertions.assertIterableEquals(project.getTechnologies(), projectDto.getTechnologies());
        Assertions.assertEquals(project.getValueAdded1(), projectDto.getValueAdded1().orElse(null));
        Assertions.assertEquals(project.getValueAdded2(), projectDto.getValueAdded2().orElse(null));
        Assertions.assertEquals(project.getValueAdded3(), projectDto.getValueAdded3().orElse(null)); 
    }

    @Test
    public void testConvertToProject() {
        var projectDto = Instancio.create(ProjectDto.class);
        var project = ProjectControllerServiceUtil.convertToProject(projectDto);

        // Assertions to verify the conversion
        Assertions.assertEquals(projectDto.getId(), project.getId());
        Assertions.assertEquals(projectDto.getTitle(), project.getTitle());
        Assertions.assertIterableEquals(projectDto.getApproach(), project.getApproachTechnologies());
        Assertions.assertIterableEquals(projectDto.getInitialSituation(), project.getInitialSituation());
        Assertions.assertIterableEquals(projectDto.getChallenges(), project.getChallenges());
        Assertions.assertIterableEquals(projectDto.getTechnologies(), project.getTechnologies());
        Assertions.assertEquals(projectDto.getValueAdded1().orElse(null), project.getValueAdded1());
        Assertions.assertEquals(projectDto.getValueAdded2().orElse(null), project.getValueAdded2());
        Assertions.assertEquals(projectDto.getValueAdded3().orElse(null), project.getValueAdded3());
    }

    @Test
    public void testUpdateProjectFromDto() {
        // Your test logic here
        var project = Instancio.create(Project.class);
        var projectDto = Instancio.create(ProjectDto.class);
        var updatedProject = ProjectControllerServiceUtil.updateProjectFromDto(project, projectDto);
        // Assertions to verify the update
        Assertions.assertEquals(projectDto.getTitle(), updatedProject.getTitle());
        Assertions.assertIterableEquals(projectDto.getApproach(), updatedProject.getApproachTechnologies());
        Assertions.assertIterableEquals(projectDto.getInitialSituation(), updatedProject.getInitialSituation());
        Assertions.assertIterableEquals(projectDto.getChallenges(), updatedProject.getChallenges());
        Assertions.assertIterableEquals(projectDto.getTechnologies(), updatedProject.getTechnologies());
        Assertions.assertEquals(projectDto.getValueAdded1().orElse(null), updatedProject.getValueAdded1());
        Assertions.assertEquals(projectDto.getValueAdded2().orElse(null), updatedProject.getValueAdded2());
        Assertions.assertEquals(projectDto.getValueAdded3().orElse(null), updatedProject.getValueAdded3());
    }
}
