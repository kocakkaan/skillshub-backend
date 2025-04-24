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
    projectDto.setValueAddedDiamond(Optional.ofNullable(project.getDiamondText()));
    projectDto.setValueAddedMoney(Optional.ofNullable(project.getMoneyText()));
    projectDto.setValueAddedGraph(Optional.ofNullable(project.getGraphText()));
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
    project.setDiamondText(projectDto.getValueAddedDiamond().orElse(null));
    project.setMoneyText(projectDto.getValueAddedMoney().orElse(null));
    project.setGraphText(projectDto.getValueAddedGraph().orElse(null));
    return project;
  }

  public static Project updateProjectFromDto(Project project, ProjectDto projectDto) {
    project.setTitle(projectDto.getTitle());
    project.setInitialSituation(projectDto.getInitialSituation());
    project.setChallenges(projectDto.getChallenges());
    project.setApproachTechnologies(projectDto.getApproach());
    project.setTechnologies(projectDto.getTechnologies());
    project.setDiamondText(projectDto.getValueAddedDiamond().orElse(null));
    project.setMoneyText(projectDto.getValueAddedMoney().orElse(null));
    project.setGraphText(projectDto.getValueAddedGraph().orElse(null));
    return project;
  }

}
