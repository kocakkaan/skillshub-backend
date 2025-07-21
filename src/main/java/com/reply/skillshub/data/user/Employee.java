package com.reply.skillshub.data.user;

import java.time.LocalDate;
import java.util.List;

import com.reply.skillshub.data.userrole.UserRole;

public interface Employee {

  public String getId();

  public String getFirstName();

  public String getLastName();

  public String getConfirmationToken();

  public boolean getConfirmed();

  public List<Resume> getResumes();

  public UserRole getUserRole();

  public String getCreatedUserId();

  public CreatedBy getCreatedBy();

  public LocalDate getCreatedOn();

  default String getFullName() {
    return getFirstName().concat(" ").concat(getLastName());
  }

  interface CreatedBy {
    public String getId();

    public String getFirstName();

    public String getLastName();

    default String getFullName() {
      return getFirstName().concat(" ").concat(getLastName());
    }

  }

  interface Resume {
    public String getId();
    public String getRole();
  }

}
