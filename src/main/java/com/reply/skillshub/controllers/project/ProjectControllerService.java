package com.reply.skillshub.controllers.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProjectPictureNotDeletedException;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProjectPictureNotFoundException;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProjectPictureNotSavedException;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.controllers.project.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.project.powerpoint.ProjectPowerPointService;
import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.data.project.ProjectReference;
import com.reply.skillshub.data.project.ProjectService;
import com.reply.skillshub.data.projectcounter.ProjectCounterService;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.openapi.model.CreateProjectDto;
import com.reply.skillshub.openapi.model.ProjectDto;
import com.reply.skillshub.services.ProjectAgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectControllerService {

  private final ProjectService projectService;
  private final ProjectCounterService projectCounterService;
  private final ProjectPowerPointService powerPointService;
  private final LoadCurrentUser loadCurrentUser;
  private final ProjectAgentService projectAgentService;

  private static final int TOP_K = 5;

  @Value("${skillhub.projectpicture.path}")
  private String projectPicturePath;

  private boolean isAdmin() {
    return loadCurrentUser.loadSkillhubUserFromContext().getUserRole() == UserRole.ADMIN;
  }

  public void deleteProject(String id) {
    if (!isAdmin()) {
      throw new InsufficientRights();
    }

    Project project = projectService.findById(id);

    if (project != null) {
      String pictureLocation = project.getProjectPictureLocation();
      if (pictureLocation != null && !pictureLocation.isEmpty()) {
        try {
          Path filePath = Paths.get(projectPicturePath).resolve(pictureLocation).normalize();
          Files.deleteIfExists(filePath);
        } catch (IOException e) {
          throw new ProjectPictureNotDeletedException("Error deleting project picture: " + pictureLocation, e);
        }
      }
    }
    projectService.deleteById(id);
  }

  public XMLSlideShow exportMultipleProjectsToPptx(String language, List<String> projects) {
    List<PowerPointInformation> dtoList = new ArrayList<>();
    for (String projectId : projects) {
      var project = projectService.findById(projectId);
      var dto = powerPointService.createPowerPointDto(project, language);
      dtoList.add(dto);
    }
    return powerPointService.createSlideShowFromMultipleTemplates(dtoList);
  }

  public XMLSlideShow exportToPptx(String projectId, String language) {
    var project = projectService.findById(projectId);
    var dto = powerPointService.createPowerPointDto(project, language);
    var ppt = powerPointService.createPowerPointFromTemplate(dto);
    return ppt;
  }

  public Resource exportToImage(String projectId, String language) {
    var project = projectService.findById(projectId);
    var dto = powerPointService.createPowerPointDto(project, language);
    var ppt = powerPointService.createPowerPointFromTemplate(dto);
    var image = powerPointService.getFirstSlideAsImage(ppt);
    return image;
  }

  public String saveProjectPicture(String projectId, MultipartFile projectPictureFile) {
    var project = projectService.findById(projectId, ProjectWithFilelocation.class);
    if (project == null) {
      throw new RuntimeException("Project not found with id: " + projectId);
    }

    File directory = new File(projectPicturePath);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    String extension = ".jpg";
    String originalFilename = projectPictureFile.getOriginalFilename();
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    String filename = project.getId() + "_picture" + extension;
    Path filePath = Paths.get(projectPicturePath, filename);

    try (InputStream in = projectPictureFile.getInputStream()) {
      Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
      project.setProjectPictureLocation(filename);
      projectService.save(project);
    } catch (IOException e) {
      throw new ProjectPictureNotSavedException("Could not save project picture: " + filename);
    }
    return "Project picture saved successfully: " + filename;
  }

  public Resource getProjectPicture(String projectId) {
    var project = projectService.findById(projectId);
    if (project == null || project.getProjectPictureLocation() == null
        || project.getProjectPictureLocation().isEmpty()) {
      throw new ProjectPictureNotFoundException("Project picture not found for project id: " + projectId);
    }

    try {
      Path filePath = Paths.get(projectPicturePath).resolve(project.getProjectPictureLocation()).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new ProjectPictureNotFoundException(
            "Could not read project picture: " + project.getProjectPictureLocation());
      }
    } catch (Exception e) {
      throw new ProjectPictureNotFoundException(
          "Error accessing project picture: " + project.getProjectPictureLocation(), e);
    }
  }

  public List<ProjectDto> getAllProjects() {
    var projects = projectService.findAll();
    return projects.stream().sorted(Comparator.comparingInt(ProjectReference::getProjectId))
        .map(ProjectControllerServiceUtil::convertToProjectDto).toList();
  }

  public List<ProjectDto> getSearchProjects(String search) {
    List<String> projectIdsFromAgent = projectAgentService.searchProjectsByQuery(search, TOP_K);
    if (projectIdsFromAgent == null || projectIdsFromAgent.isEmpty()) {
      return Collections.emptyList();
    }
    List<ProjectDto> projectReferences = projectIdsFromAgent.stream()
        .map(projectService::findReferenceById)
        .filter(Objects::nonNull)
        .map(ProjectControllerServiceUtil::convertToProjectDto)
        .toList();

    return projectReferences;
  }

  public ProjectDto createProject(CreateProjectDto projectDto) {
    var project = new Project();
    project.setTitle(projectDto.getTitle());
    var nextProjectId = projectCounterService.incrementProjectId();
    project.setProjectId(nextProjectId);
    var savedProject = projectService.save(project);
    var projectReference = projectService.findReferenceById(savedProject.getId());
    return ProjectControllerServiceUtil.convertToProjectDto(projectReference);
  }

  public ProjectDto updateProject(String id, ProjectDto projectDto) {
    var project = projectService.findById(id);
    if (project != null) {
      ProjectControllerServiceUtil.updateProjectFromDto(project, projectDto);
      var updatedProject = projectService.save(project);
      projectAgentService.processProject(updatedProject.getId());
      var projectReference = projectService.findReferenceById(updatedProject.getId());
      return ProjectControllerServiceUtil.convertToProjectDto(projectReference);
    }
    return null;
  }

  public ProjectDto getProjectById(String id) {
    var project = projectService.findReferenceById(id);
    return ProjectControllerServiceUtil.convertToProjectDto(project);
  }

}
