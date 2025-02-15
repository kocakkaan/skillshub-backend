package com.reply.skillshub.controllers.users;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.skill.Skill;

import lombok.Data;

@Data
public class UserWithSkills {

  private String id;
  private List<Skill> skills = new ArrayList<>();
 
}
