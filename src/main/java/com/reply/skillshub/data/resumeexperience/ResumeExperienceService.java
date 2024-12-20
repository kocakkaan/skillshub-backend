package com.reply.skillshub.data.resumeexperience;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeExperienceService {
  
  private final ResumeExperienceRepository repository;

  public void deleteById(String id) {
    repository.deleteById(id);
  }

  public List<ResumeExperience> findAllByResumesId(String resumeId) {
    return repository.findAllByResumesId(resumeId);
  }
}
