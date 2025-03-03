package com.reply.skillshub.controllers.experience;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.experience.Experience;

import lombok.Data;

@Data
public class UserWithExperiences {

  private String id;
  private List<Experience> experiences = new ArrayList<>();
  
}
