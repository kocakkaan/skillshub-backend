package com.reply.skillshub.controllers.resume;

import java.util.List;

import com.reply.skillshub.data.resumeskill.ResumeSkill;

import lombok.Data;

@Data
public class ResumeWithResumeSkills {

  private String id;
  private List<ResumeSkill> skills; 
  
}
