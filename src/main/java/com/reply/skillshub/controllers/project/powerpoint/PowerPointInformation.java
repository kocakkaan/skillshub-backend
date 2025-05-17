package com.reply.skillshub.controllers.project.powerpoint;

import java.util.List;

import lombok.Data;

@Data
public class PowerPointInformation {

  private String language;

  private String projectId;
  private String projectTitle;
  private String projectPictureLocation;

  private List<String> initialSituation;
  private List<String> challenges;
  private List<String> approachTechnologies;

  private String valueAddedText0;
  private String valueAddedText1;
  private String valueAddedText2;

}
