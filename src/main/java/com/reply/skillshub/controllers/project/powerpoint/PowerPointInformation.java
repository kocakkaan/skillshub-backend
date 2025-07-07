package com.reply.skillshub.controllers.project.powerpoint;

import java.util.List;

import lombok.Data;

@Data
public class PowerPointInformation {

  private String language;

  private String projectId;
  private String projectTitle;
  private String projectPictureLocation;

  private List<String> initialSituation = List.of();
  private List<String> challenges = List.of();
  private List<String> approachTechnologies = List.of();

  private String valueAddedText0;
  private String valueAddedText1;
  private String valueAddedText2;

}
