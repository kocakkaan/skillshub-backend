package com.reply.skillshub.controllers.users;

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

  public String getFullname() {
    return this.firstName + " " + this.lastName;
  }

}
