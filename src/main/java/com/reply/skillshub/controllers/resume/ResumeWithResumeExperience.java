package com.reply.skillshub.controllers.resume;

import java.util.List;

import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;

import lombok.Data;

@Data
public class ResumeWithResumeExperience {

  private String id;
  private List<ResumeExperience> experiences; 
  
}
