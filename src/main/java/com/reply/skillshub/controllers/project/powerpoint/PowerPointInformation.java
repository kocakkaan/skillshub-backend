package com.reply.skillshub.controllers.project.powerpoint;

import java.util.List;

import lombok.Data;

@Data
public class PowerPointInformation {

  private String language;

  private String projectId;
  private String projectTitle;

  private List<String> initialSituation;
  private List<String> challenges;
  private List<String> approachTechnologies;

  private String diamondText;
  private String moneyText;
  private String graphText;
  
}
