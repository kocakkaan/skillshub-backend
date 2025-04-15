package com.reply.skillshub.controllers.resume.interfaces;

import java.util.List;

public interface UserWithShortCvs {

    String getId();
    String getFirstName();
    String getLastName();

    List<ShortCvWithBaseResume> getResumes();

    default String getFullName() {
        return getFirstName().concat(" ").concat(getLastName());
    }

    interface ShortCvWithBaseResume {

        String getId();

        String getTitle();

        String getRole();

    }
  
}
