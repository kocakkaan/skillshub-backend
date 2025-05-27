package com.reply.skillshub;

import java.time.LocalDate;

import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.userrole.UserRole;

public class BaseUserImp implements BaseUser {
  private String id;
  private String firstName;
  private String lastName;
  private String profilePictureLocation;
  private String email;
  private String phoneNumber;
  private String password;
  private String confirmationToken;
  private Boolean confirmed;
  private BaseUser createdBy;
  private LocalDate createdOn;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getProfilePictureLocation() {
    return profilePictureLocation;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public UserRole getUserRole() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getConfirmationToken() {
    return confirmationToken;
  }

  @Override
  public Boolean getConfirmed() {
    return confirmed;
  }

  @Override
  public BaseUser getCreatedBy() {
    return createdBy;
  }

  @Override
  public LocalDate getCreatedOn() {
    return createdOn;
  }
}
