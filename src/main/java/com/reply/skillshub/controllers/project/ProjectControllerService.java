package com.reply.skillshub.controllers.project;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.project.ProjectService;
import com.reply.skillshub.openapi.model.ProjectDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectControllerService {

  private final ProjectService projectService;

  public void deleteProject(String id) {
    projectService.deleteById(id);
  }

  public List<ProjectDto> getAllProjects() {
    var projects = projectService.findAll();
    return projects.stream().map(ProjectControllerServiceUtil::convertToProjectDto).toList();
  }

  public ProjectDto createProject(ProjectDto projectDto) {
    var project = ProjectControllerServiceUtil.convertToProject(projectDto);
    var savedProject = projectService.save(project);
    return ProjectControllerServiceUtil.convertToProjectDto(savedProject);
  }

  public ProjectDto updateProject(String id, ProjectDto projectDto) {
    var project = projectService.findById(id);
    if (project != null) {
      ProjectControllerServiceUtil.updateProjectFromDto(project, projectDto);
      var updatedProject = projectService.save(project);
      return ProjectControllerServiceUtil.convertToProjectDto(updatedProject);
    }
    return null;
  }

  public ProjectDto getProjectById(String id) {
    var project = projectService.findById(id);
    return ProjectControllerServiceUtil.convertToProjectDto(project);
  }

}
