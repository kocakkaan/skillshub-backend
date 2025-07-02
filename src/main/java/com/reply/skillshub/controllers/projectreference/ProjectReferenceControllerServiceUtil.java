package com.reply.skillshub.controllers.projectreference;

import java.util.Optional;

import com.reply.skillshub.data.project.reference.ProjectReference;
import com.reply.skillshub.openapi.model.ProjectReferenceDto;

public class ProjectReferenceControllerServiceUtil {

  public static ProjectReferenceDto convertToProjectReferenceDto(ProjectReference project) {
    ProjectReferenceDto projectReferenceDto = new ProjectReferenceDto();
    projectReferenceDto.setId(project.getId());
    projectReferenceDto.setTitle(project.getTitle());
    projectReferenceDto.setLanguage(project.getLanguage());
    projectReferenceDto.setInitialSituation(project.getInitialSituation());
    projectReferenceDto.setChallenges(project.getChallenges());
    projectReferenceDto.setApproach(project.getApproachTechnologies());
    projectReferenceDto.setValueAdded0(Optional.ofNullable(project.getValueAddedText0()));
    projectReferenceDto.setValueAdded1(Optional.ofNullable(project.getValueAddedText1()));
    projectReferenceDto.setValueAdded2(Optional.ofNullable(project.getValueAddedText2()));
    return projectReferenceDto;
  }

  public static ProjectReference convertToProjectReference(ProjectReferenceDto projectReferenceDto) {
    return convertToProjectReference(projectReferenceDto, null);
  }

  public static ProjectReference convertToProjectReference(ProjectReferenceDto projectReferenceDto, ProjectReference projectReference) {
    if (projectReference == null) {
      projectReference = new ProjectReference();
    }
    projectReference.setId(projectReferenceDto.getId());
    projectReference.setTitle(projectReferenceDto.getTitle());
    projectReference.setLanguage(projectReferenceDto.getLanguage());
    projectReference.setInitialSituation(projectReferenceDto.getInitialSituation());
    projectReference.setChallenges(projectReferenceDto.getChallenges());
    projectReference.setApproachTechnologies(projectReferenceDto.getApproach());
    projectReference.setValueAddedText0(projectReferenceDto.getValueAdded0().orElse(null));
    projectReference.setValueAddedText1(projectReferenceDto.getValueAdded1().orElse(null));
    projectReference.setValueAddedText2(projectReferenceDto.getValueAdded2().orElse(null));
    return projectReference;
  }

  public static ProjectReference updateProjectReferenceFromDto(ProjectReference project, ProjectReferenceDto projectDto) {
    return convertToProjectReference(projectDto, project);
  }

}
