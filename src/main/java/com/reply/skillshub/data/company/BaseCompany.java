package com.reply.skillshub.data.company;

import java.util.List;

import com.reply.skillshub.data.user.BaseUser;

public interface BaseCompany {
  public String getId();

  public String getLabel();

  public List<BaseUser> getEmployees();
}
