package com.reply.skillshub.controllers.users.profileextractor;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.speaks.Speaks;

import lombok.Data;

@Data
public class UserProfile {

  private String id;
  private List<Skill> skills = new ArrayList<>();
  private List<Speaks> speaks = new ArrayList<>();
  private List<BaseExperience> experiences = new ArrayList<>();
  private List<HasCertificate> hasCertificates = new ArrayList<>();

  @Data
  public static class BaseExperience {
    private String id;
  }
  
}
