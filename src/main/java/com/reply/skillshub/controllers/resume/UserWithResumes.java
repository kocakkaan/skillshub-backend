package com.reply.skillshub.controllers.resume;

import java.util.List;

public interface UserWithResumes {

  String getId();

  List<Resume> getResumes();

  public interface Resume {
    String getId();

    String getTitle();
  }
}
