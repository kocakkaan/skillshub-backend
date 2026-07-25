package com.reply.skillshub.controllers.project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reply.skillshub.openapi.api.ProjectsApi;
import com.reply.skillshub.openapi.model.CreateProjectDto;
import com.reply.skillshub.openapi.model.ProjectDto;
import com.reply.skillshub.openapi.model.UpdateProjectDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectsApi {

  private final ProjectControllerService projectControllerService;
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectController.class);

  @Override
  public ResponseEntity<List<ProjectDto>> projectsGet(Optional<String> search) {
    if (search.isPresent() && !search.get().isEmpty()) {
      return ResponseEntity.ok(projectControllerService.getSearchProjects(search.get()));
    } else {
      return ResponseEntity.ok(projectControllerService.getAllProjects());
    }
  }

  @Override
  public ResponseEntity<ProjectDto> projectsPost(CreateProjectDto createProjectDto) {
    return ResponseEntity.ok(projectControllerService.createProject(createProjectDto));
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
  public ResponseEntity<ProjectDto> projectsProjectIdPut(String projectId, UpdateProjectDto projectDto) {
    return ResponseEntity.ok(projectControllerService.updateProject(projectId, projectDto));
  }

  @Override
  public ResponseEntity<Resource> exportMultipleProjectsToPptx(String language,
      List<String> projects) {
    var ppt = projectControllerService.exportMultipleProjectsToPptx(language, projects);
    var boas = new ByteArrayOutputStream();
    try {
      ppt.write(boas);
      ppt.close();
    } catch (IOException e) {
      logger.error("Error while writing PowerPoint to ByteArrayOutputStream", e);
    }

    Resource resource = new ByteArrayResource(boas.toByteArray());

    // Set the response headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(
        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
    headers.setContentDispositionFormData("attachment", "presentation.pptx");

    // Return the presentation as a response entity
    return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
  }

  @Override
  public ResponseEntity<Resource> exportToPptx(String projectId, String language) {
    var ppt = projectControllerService.exportToPptx(projectId, language);
    var boas = new ByteArrayOutputStream();
    try {
      ppt.write(boas);
      ppt.close();
    } catch (IOException e) {
      logger.error("Error while writing PowerPoint to ByteArrayOutputStream", e);
    }

    Resource resource = new ByteArrayResource(boas.toByteArray());

    // Set the response headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(
        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
    headers.setContentDispositionFormData("attachment", "presentation.pptx");

    // Return the presentation as a response entity
    return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
  }

  @Override
  public ResponseEntity<Resource> projectsProjectIdExportPreviewPost(String projectId,
      String language) {
    var image = projectControllerService.exportToImage(projectId, language);
    return ResponseEntity.ok(image);
  }

  @Override
  public ResponseEntity<String> uploadProjectPicture(String projectId, MultipartFile file) {
    String message = projectControllerService.saveProjectPicture(projectId, file);
    return ResponseEntity.ok(message);
  }

  @Override
  public ResponseEntity<Resource> getProjectPicture(String projectId) {
    Resource file = projectControllerService.getProjectPicture(projectId);
    String contentType = null;
    try {
      contentType = Files.probeContentType(file.getFile().toPath());
    } catch (IOException | UnsupportedOperationException e) {
      logger.warn("Could not determine content type for project picture {}", projectId, e);
    }
    if (contentType == null) {
      contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }

}
