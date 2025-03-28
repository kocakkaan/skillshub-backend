package com.reply.skillshub.controllers.experience;

import java.util.List;

public interface BaseExperience {

  public String getId();

  public String getTitle();

  public List<String> getDescriptions();
  // public Optional<LocalDate> getStartDate();
  // public Optional<LocalDate> getEndDate();

  public List<Skill> getSkills();

  public List<Occupation> getOccupation();

  interface Skill {
    public String getId();

    public String getLabel();
  }

  interface Occupation {
    public String getId();

    public String getLabel();
  }

}