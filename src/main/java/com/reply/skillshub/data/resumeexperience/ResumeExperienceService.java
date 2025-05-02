package com.reply.skillshub.data.resumeexperience;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ExperienceNotFound;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeExperienceService {

  private final ResumeExperienceRepository repository;

  public void deleteById(String id) {
    repository.deleteById(id);
  }

  public ResumeExperience findById(String resumeExperienceId) {
    return repository.findById(resumeExperienceId).orElseThrow(ExperienceNotFound::new);
  }

  public List<ResumeExperience> findAllByIds(List<String> ids) {
    return repository.findAllById(ids);
  }

  public ResumeExperience save(ResumeExperience resumeExperience) {
    return repository.save(resumeExperience);
  }
}
