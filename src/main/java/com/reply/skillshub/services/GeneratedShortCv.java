package com.reply.skillshub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeneratedShortCv {

  @JsonProperty("professional_background")
  private String professionalBackground;
  private HashMap<String, List<String>> skills = new HashMap<>();
  private List<CvExperience> experiences = new ArrayList<>();
  private List<String> industries = new ArrayList<>();

  @Data
  public static class CvExperience {
    String title;
    String role;
    List<String> descriptions;
  }

}
