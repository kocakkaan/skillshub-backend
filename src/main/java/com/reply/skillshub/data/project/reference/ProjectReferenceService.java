package com.reply.skillshub.data.project.reference;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectReferenceService {

  private final ProjectReferenceRepository projectReferenceRepository;

  public ProjectReference findById(String id) {
    return projectReferenceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Project reference not found with id: " + id));
  }

  public ProjectReference save(ProjectReference projectReference) {
    return projectReferenceRepository.save(projectReference);
  }


  
}
