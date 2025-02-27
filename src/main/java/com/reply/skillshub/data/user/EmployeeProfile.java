package com.reply.skillshub.data.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeProfile extends Employee {

  public String getEmail();
  public String getPhoneNumber();
  public List<Skill> getSkills();
  public List<Experience> getExperiences();
  public List<Resume> getResumes();
  public List<HasCertificate> getHasCertificates();


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

  interface HasCertificate {
    public String getId();
    public Certificate getCertificate();
    public LocalDate getExpirationDate();
    public String getFile();
    public LocalDate getIssuedDate();
  }

  interface Certificate {
    public String getId();
    public String getName();
    public String getIssuer();
  }
  
}