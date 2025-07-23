package com.reply.skillshub.controllers.project;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.industry.IndustryService;
import com.reply.skillshub.data.project.Project;
import com.reply.skillshub.data.skill.SkillService;
import com.reply.skillshub.openapi.model.UpdateProjectDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectConverterService {

  private final IndustryService industryService;
  private final SkillService skillService;

  public Project updateProjectFromDto(Project project, UpdateProjectDto projectDto) {
    project.setTitle(projectDto.getTitle());
    project.setDescription(projectDto.getDescription().orElse(null));
    
    var skills = projectDto.getTechnologies().stream().map(skillService::findByLabelIgnoreCase)
        .flatMap(optionalSkill -> optionalSkill.map(Stream::of).orElseGet(Stream::empty)) // Stream<Skill>
        .toList();
    project.setTechnologies(skills);

    projectDto.getIndustry().ifPresent(nullity -> {
      var industry = industryService.findByLabel(nullity);
      if (industry != null && !industry.isEmpty()) {
        project.setIndustry(industry.get(0));
      }
    });


    return project;
  }

}
