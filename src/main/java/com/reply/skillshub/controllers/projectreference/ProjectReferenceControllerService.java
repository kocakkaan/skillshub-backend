package com.reply.skillshub.controllers.projectreference;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.project.reference.ProjectReferenceService;
import com.reply.skillshub.openapi.model.ProjectReferenceDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectReferenceControllerService {

  private final ProjectReferenceService projectReferenceService;

  public ProjectReferenceDto updateProjectReference(String referenceId, ProjectReferenceDto projectReferenceDto) {
    var existingReference = projectReferenceService.findById(referenceId);
    var updatedReference = ProjectReferenceControllerServiceUtil.updateProjectReferenceFromDto(existingReference, projectReferenceDto);
    var savedReference = projectReferenceService.save(updatedReference);
    return ProjectReferenceControllerServiceUtil.convertToProjectReferenceDto(savedReference);
    
  }
  
}
