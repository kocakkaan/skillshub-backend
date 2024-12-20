package com.reply.skillshub.data.resumeskill;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeSkillNotFound;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeSkillService {

  private final ResumeSkillRepository repository;

  public void deleteById(String id) {
    repository.deleteById(id);
  }

  public ResumeSkill findById(String id) {
    return repository.findById(id).orElseThrow(ResumeSkillNotFound::new);
  }

  public List<ResumeSkill> findAllByResumesId(String resumeId) {
    return repository.findAllByResumesId(resumeId);
  }

  public ResumeSkill save(ResumeSkill resumeSkill) {
    return repository.save(resumeSkill);
  }
  
}
