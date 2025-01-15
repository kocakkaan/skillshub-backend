package com.reply.skillshub.data.user;

import java.util.List;

public interface EmployeeProfile extends Employee {

  public String getEmail();
  public String getPhoneNumber();
  public List<Skill> getSkills();
  public List<Experience> getExperiences();
  public List<Resume> getResumes();
  public List<Certificate> getCertificates();


  interface Skill {
    public String getId();
    public String getLabel();
  }

  interface Experience {
    public String getId();
    public String getTitle();
    public List<String> getDescriptions();
    public List<Occupation> getOccupation();
    public List<Skill> getSkills();

  }

  interface Occupation {
    public String getId();
    public String getLabel();
  }

  interface Resume {
    public String getId();
    public String getTitle();
  }

  interface Certificate {
    public String getId();
    public String getLabel();
  }
  
}