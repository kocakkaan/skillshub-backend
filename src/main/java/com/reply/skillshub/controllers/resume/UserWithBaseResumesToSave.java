package com.reply.skillshub.controllers.resume;

import java.util.List;

import lombok.Data;

@Data
public class UserWithBaseResumesToSave {

  private String id;
  private List<BaseResume> resumes;

}
