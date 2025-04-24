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
        Assertions.assertEquals(project.getDiamondText(), projectDto.getValueAddedDiamond().orElse(null));
        Assertions.assertEquals(project.getMoneyText(), projectDto.getValueAddedMoney().orElse(null));
        Assertions.assertEquals(project.getGraphText(), projectDto.getValueAddedGraph().orElse(null));
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
        Assertions.assertEquals(projectDto.getValueAddedDiamond().orElse(null), project.getDiamondText());
        Assertions.assertEquals(projectDto.getValueAddedMoney().orElse(null), project.getMoneyText());
        Assertions.assertEquals(projectDto.getValueAddedGraph().orElse(null), project.getGraphText());
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
        Assertions.assertEquals(projectDto.getValueAddedDiamond().orElse(null), updatedProject.getDiamondText());
        Assertions.assertEquals(projectDto.getValueAddedMoney().orElse(null), updatedProject.getMoneyText());
        Assertions.assertEquals(projectDto.getValueAddedGraph().orElse(null), updatedProject.getGraphText());
    }
}
