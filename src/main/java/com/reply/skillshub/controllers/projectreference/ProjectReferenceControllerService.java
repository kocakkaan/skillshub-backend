package com.reply.skillshub.controllers.projectreference;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.project.ProjectService;
import com.reply.skillshub.data.project.reference.ProjectReferenceService;
import com.reply.skillshub.openapi.model.ProjectReferenceDto;
import com.reply.skillshub.services.ProjectAgentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectReferenceControllerService {

  private final ProjectReferenceService projectReferenceService;
  private final ProjectService projectService;
  private final ProjectAgentService projectAgentService;

  public ProjectReferenceDto updateProjectReference(String referenceId, ProjectReferenceDto projectReferenceDto) {
    var existingReference = projectReferenceService.findById(referenceId);
    var updatedReference = ProjectReferenceControllerServiceUtil.updateProjectReferenceFromDto(existingReference,
        projectReferenceDto);
    var savedReference = projectReferenceService.save(updatedReference);

    var project = projectService.findByProjectReferenceId(referenceId);
    if (project.isPresent()) {
      projectAgentService.translateProject(project.get().getId(), savedReference.getLanguage());
    }

    return ProjectReferenceControllerServiceUtil.convertToProjectReferenceDto(savedReference);
  }
}
