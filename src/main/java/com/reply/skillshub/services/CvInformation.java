package com.reply.skillshub.services;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CvInformation {

  private List<String> skills = new ArrayList<>();
  private List<Language> languages = new ArrayList<>();
  private List<Experience> experiences = new ArrayList<>();
  private List<Certificate> certificates = new ArrayList<>();

  @Data
  public static class Experience {
    String title;
    String company;
    String start_date;
    String end_date;
    List<String> descriptions;
    String summary;
  }

  @Data
  public static class Certificate {
    private String name;
    private String issuer;
    private String issued_date;
    private String expiration_date;
  }

  @Data
  public static class Language {
    private String language;
    private Boolean is_native;
    private String proficiency;
  }

}
