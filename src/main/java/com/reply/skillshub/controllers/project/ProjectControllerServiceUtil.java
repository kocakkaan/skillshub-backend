package com.reply.skillshub.controllers.project;

import java.util.Optional;

import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.openapi.model.ProjectDto;

public class ProjectControllerServiceUtil {

  public static ProjectDto convertToProjectDto(Project project) {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setId(project.getId());
    projectDto.setTitle(project.getTitle());
    projectDto.setInitialSituation(project.getInitialSituation());
    projectDto.setChallenges(project.getChallenges());
    projectDto.setApproach(project.getApproachTechnologies());
    projectDto.setTechnologies(project.getTechnologies());
    projectDto.setValueAdded1(Optional.ofNullable(project.getValueAdded1()));
    projectDto.setValueAdded2(Optional.ofNullable(project.getValueAdded2()));
    projectDto.setValueAdded3(Optional.ofNullable(project.getValueAdded3()));
    return projectDto;
  }

  public static Project convertToProject(ProjectDto projectDto) {
    Project project = new Project();
    project.setId(projectDto.getId());
    project.setTitle(projectDto.getTitle());
    project.setInitialSituation(projectDto.getInitialSituation());
    project.setChallenges(projectDto.getChallenges());
    project.setApproachTechnologies(projectDto.getApproach());
    project.setTechnologies(projectDto.getTechnologies());
    project.setValueAdded1(projectDto.getValueAdded1().orElse(null));
    project.setValueAdded2(projectDto.getValueAdded2().orElse(null));
    project.setValueAdded3(projectDto.getValueAdded3().orElse(null));
    return project;
  }

  public static Project updateProjectFromDto(Project project, ProjectDto projectDto) {
    project.setTitle(projectDto.getTitle());
    project.setInitialSituation(projectDto.getInitialSituation());
    project.setChallenges(projectDto.getChallenges());
    project.setApproachTechnologies(projectDto.getApproach());
    project.setTechnologies(projectDto.getTechnologies());
    project.setValueAdded1(projectDto.getValueAdded1().orElse(null));
    project.setValueAdded2(projectDto.getValueAdded2().orElse(null));
    project.setValueAdded3(projectDto.getValueAdded3().orElse(null));
    return project;
  }


  
}
