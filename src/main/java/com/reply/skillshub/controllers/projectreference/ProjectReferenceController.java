package com.reply.skillshub.controllers.projectreference;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ReferencesApi;
import com.reply.skillshub.openapi.model.ProjectReferenceDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectReferenceController implements ReferencesApi{

  private final ProjectReferenceControllerService projectReferenceControllerService;

  @Override
  public ResponseEntity<ProjectReferenceDto> projectsReferencesReferenceIdPut(String referenceId,
      @Valid ProjectReferenceDto projectReferenceDto) {
        return ResponseEntity.ok(
            projectReferenceControllerService.updateProjectReference(referenceId, projectReferenceDto));
  }
  
}
