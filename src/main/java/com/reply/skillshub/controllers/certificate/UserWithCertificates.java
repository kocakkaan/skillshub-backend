package com.reply.skillshub.controllers.certificate;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.hascertificate.HasCertificate;

import lombok.Data;

@Data
public class UserWithCertificates {

  private String id;
  private List<HasCertificate> hasCertificates = new ArrayList<>();
  
}
