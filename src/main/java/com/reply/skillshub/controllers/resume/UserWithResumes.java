package com.reply.skillshub.controllers.resume;

import java.util.List;


import lombok.Data;

@Data
public class UserWithResumes {

  private String id;
  private List<BaseResume> resumes;

  @Data
  public static class BaseResume {
    private String id;
    private String title;
  }

}
