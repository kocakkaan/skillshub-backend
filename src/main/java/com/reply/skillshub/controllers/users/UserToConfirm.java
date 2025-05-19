package com.reply.skillshub.controllers.users;

import java.time.LocalDate;

import com.reply.skillshub.data.userrole.UserRole;

import lombok.Data;

@Data
public class UserToConfirm {

  private String id;
  private String password;
  private boolean confirmed;
  private String confirmationToken;
  private String email;
  private String firstName;
  private String lastName;
  private UserRole userRole;
  private String profilePictureLocation;
  private String createdBy;
  private LocalDate createdOn;

  public String getFullname() {
    return this.firstName + " " + this.lastName;
  }

}
