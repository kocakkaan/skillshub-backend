package com.reply.skillshub.controllers.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.data.project.ProjectRead;
import com.reply.skillshub.data.project.ProjectReadWithReference;
import com.reply.skillshub.data.project.reference.ProjectReference;
import com.reply.skillshub.openapi.model.ClientDto;
import com.reply.skillshub.openapi.model.ProjectDto;
import com.reply.skillshub.openapi.model.ProjectReferenceDto;
import com.reply.skillshub.openapi.model.UpdateProjectDto;

public class ProjectControllerServiceUtil {

  public static ProjectDto convertProjectDtoWithReferences(ProjectReadWithReference project) {
    var projectDto = convertToProjectDto(project);
    projectDto.setProjectReferences(project.getReferences().stream()
          .map(ProjectControllerServiceUtil::convertToProjectReferenceDto).toList());
    return projectDto;
  }

  public static ProjectDto convertToProjectDto(ProjectRead project) {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setId(project.getId());
    projectDto.setProjectId(project.getFormattedProjectId());
    projectDto.setTitle(project.getTitle());
    projectDto.setDescription(Optional.ofNullable(project.getDescription()));
    projectDto.setIndustries(Optional.ofNullable(project.getIndustry() != null ? project.getIndustry().getLabel() : null));

    if (project.getProjectPictureLocation() != null && !project.getProjectPictureLocation().isEmpty()) {
      projectDto.setProjectPictureUrl(Optional.of("/api/projects/" + project.getId() + "/picture"));
    } else {
      projectDto.setProjectPictureUrl(Optional.empty());
    }

    List<ClientDto> clients = new ArrayList<>();
    if (project.getClients() != null) {
      for (var clientEntity : project.getClients()) {
        ClientDto client = new ClientDto();
        client.setId(clientEntity.getId());
        client.setName(clientEntity.getName());
        clients.add(client);
      }
    }

    projectDto.setClients(clients);
    projectDto.setProjectType(project.getProjectType().stream().map(type -> type.getName()).toList());
    projectDto.setTechnologies(project.getTechnologies().stream().map(skill -> skill.getLabel()).toList());
    return projectDto;
  }

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
    ProjectReference projectReference = new ProjectReference();
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

  public static Project updateProjectFromDto(Project project, UpdateProjectDto projectDto) {
    project.setTitle(projectDto.getTitle());
    project.setDescription(projectDto.getDescription().orElse(null));
    return project;
  }

}
