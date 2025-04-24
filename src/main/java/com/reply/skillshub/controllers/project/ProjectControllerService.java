package com.reply.skillshub.controllers.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.reply.skillshub.controllers.project.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.project.powerpoint.ProjectPowerPointService;
import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.data.project.ProjectService;
import com.reply.skillshub.openapi.model.CreateProjectDto;
import com.reply.skillshub.openapi.model.ProjectDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectControllerService {

  private final ProjectService projectService;
  private final ProjectPowerPointService powerPointService;

  public void deleteProject(String id) {
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


  public List<ProjectDto> getAllProjects() {
    var projects = projectService.findAll();
    return projects.stream().map(ProjectControllerServiceUtil::convertToProjectDto).toList();
  }

  public ProjectDto createProject(CreateProjectDto projectDto) {
    var project = new Project();
    project.setTitle(projectDto.getTitle());
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
