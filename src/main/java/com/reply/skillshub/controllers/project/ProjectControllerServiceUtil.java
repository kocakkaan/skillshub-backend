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
    projectDto.setValueAdded0(Optional.ofNullable(project.getValueAddedText0()));
    projectDto.setValueAdded1(Optional.ofNullable(project.getValueAddedText1()));
    projectDto.setValueAdded2(Optional.ofNullable(project.getValueAddedText2()));

    if (project.getProjectPictureLocation() != null && !project.getProjectPictureLocation().isEmpty()) {
      projectDto.setProjectPictureUrl(Optional.of("/api/projects/" + project.getId() + "/picture"));
    } else {
      projectDto.setProjectPictureUrl(Optional.empty());
    }

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
    project.setValueAddedText0(projectDto.getValueAdded0().orElse(null));
    project.setValueAddedText1(projectDto.getValueAdded1().orElse(null));
    project.setValueAddedText2(projectDto.getValueAdded2().orElse(null));
    return project;
  }

  public static Project updateProjectFromDto(Project project, ProjectDto projectDto) {
    project.setTitle(projectDto.getTitle());
    project.setInitialSituation(projectDto.getInitialSituation());
    project.setChallenges(projectDto.getChallenges());
    project.setApproachTechnologies(projectDto.getApproach());
    project.setTechnologies(projectDto.getTechnologies());
    project.setValueAddedText0(projectDto.getValueAdded0().orElse(null));
    project.setValueAddedText1(projectDto.getValueAdded1().orElse(null));
    project.setValueAddedText2(projectDto.getValueAdded2().orElse(null));
    return project;
  }

}
