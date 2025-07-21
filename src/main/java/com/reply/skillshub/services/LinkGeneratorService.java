package com.reply.skillshub.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.user.Employee;
import com.reply.skillshub.data.user.User;

@Service
public class LinkGeneratorService {

  @Value("${skillhub.frontend}")
  private String frontendServer;

  @Value("${skillhub.server}")
  private String backendServer;

  public String getConfirmationLink(Employee employee) {
    return getConfirmationLink(employee.getConfirmationToken());
  }

  public String getConfirmationLink(BaseUser user) {
    return getConfirmationLink(user.getConfirmationToken());
  }

  public String getConfirmationLink(User user) {
    return getConfirmationLink(user.getConfirmationToken());
  }

  public String getConfirmationLink(String confirmationToken) {
    var stringBuilder = new StringBuilder();
    stringBuilder.append(frontendServer);
    stringBuilder.append("/confirm/");
    stringBuilder.append(confirmationToken);
    return stringBuilder.toString();
  }

}
