package com.reply.skillshub.controllers.users;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.speaks.Speaks;

import lombok.Data;

@Data
public class UserWithSpeaks {

  private String id;
  private List<Speaks> speaks = new ArrayList<>();

}
