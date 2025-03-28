package com.reply.skillshub.controllers.resume;

import java.util.List;

import lombok.Data;

@Data
public class ResumeWithIndustries {

  private String id; // resume id
  private List<String> industries; 
  
}
