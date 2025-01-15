package com.reply.skillshub.data.user;

import com.reply.skillshub.data.userrole.UserRole;

public interface Employee {
  
  public String getId();
  
  public String getFirstName();

  public String getLastName();
  
  public UserRole getUserRole();

  default String getFullName() {
    return getFirstName().concat(" ").concat(getLastName());
  }

  
}