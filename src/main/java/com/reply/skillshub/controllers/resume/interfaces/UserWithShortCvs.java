package com.reply.skillshub.controllers.resume.interfaces;

import java.util.List;

public interface UserWithShortCvs {

    String getId();

    List<ShortCvWithBaseResume> getResumes();

    interface ShortCvWithBaseResume {

        String getId();

        String getTitle();

        String getRole();

    }
  
}
