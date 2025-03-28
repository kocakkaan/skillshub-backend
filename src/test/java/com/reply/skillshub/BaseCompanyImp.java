package com.reply.skillshub;

import java.util.List;

import com.reply.skillshub.data.company.BaseCompany;
import com.reply.skillshub.data.user.BaseUser;

public class BaseCompanyImp implements BaseCompany {
  private String id;
  private String label;
  private List<BaseUser> employees;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public List<BaseUser> getEmployees() {
    return employees;
  }

}
