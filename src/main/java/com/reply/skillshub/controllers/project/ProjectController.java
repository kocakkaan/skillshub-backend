package com.reply.skillshub.controllers.project;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ProjectsApi;
import com.reply.skillshub.openapi.model.ProjectDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectsApi {

  private final ProjectControllerService projectControllerService;

  @Override
  public ResponseEntity<List<ProjectDto>> projectsGet(@Valid Optional<String> search) {
    return ResponseEntity.ok(projectControllerService.getAllProjects());
  }

  @Override
  public ResponseEntity<ProjectDto> projectsPost(@Valid ProjectDto projectDto) {
    return ResponseEntity.ok(projectControllerService.createProject(projectDto));
  }

  @Override
  public ResponseEntity<Void> projectsProjectIdDelete(String projectId) {
    projectControllerService.deleteProject(projectId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ProjectDto> projectsProjectIdGet(String projectId) {
    return ResponseEntity.ok(projectControllerService.getProjectById(projectId));
  }

  @Override
  public ResponseEntity<ProjectDto> projectsProjectIdPut(String projectId, @Valid ProjectDto projectDto) {
    return ResponseEntity.ok(projectControllerService.updateProject(projectId, projectDto));
  }

}
