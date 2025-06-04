package com.reply.skillshub.controllers.resume;

import java.time.LocalDate;

import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.userrole.UserRole;

import lombok.Data;

@Data
public class BaseUserTest implements BaseUser {

  private String id;
  private String firstName;
  private String lastName;
  private String profilePictureLocation;
  private String email;
  private String phoneNumber;
  private UserRole userRole;
  private String password;
  private String confirmationToken;
  private Boolean confirmed;
  private CreatedBy createdBy;
  private LocalDate createdOn;

  public String getFullName() {
    return getFirstName().concat(" ").concat(getLastName());
  }
}
