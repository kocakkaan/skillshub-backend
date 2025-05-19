package com.reply.skillshub.data.user;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.LanguageCode;

public interface EmployeeProfile extends Employee {

  public String getEmail();

  public String getPhoneNumber();

  public List<Skill> getSkills();

  public List<Experience> getExperiences();

  public List<Resume> getResumes();

  public List<HasCertificate> getHasCertificates();

  public List<Speaks> getSpeaks();

  public List<Industry> getIndustries();

  public String getCreatedBy();

  public LocalDate getCreatedOn();

  interface Industry {
    public String getId();

    public String getLabel();
  }

  interface Speaks {
    public String getId();

    public Language getLanguage();

    public boolean isNative();
  }

  interface Language {
    public String getLanguageName();

    public LanguageCode getLanguageCode();
  }

  interface Skill {
    public String getId();

    public String getLabel();
  }

  interface Experience {
    public String getId();

    public String getTitle();

    public List<String> getDescriptions();

    public Occupation getOccupation();

    public List<Skill> getSkills();

    public LocalDate getStartDate();

    public LocalDate getEndDate();

    public List<Industry> getIndustries();
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
