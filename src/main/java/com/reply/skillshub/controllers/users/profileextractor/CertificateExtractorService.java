package com.reply.skillshub.controllers.users.profileextractor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.certificate.Certificate;
import com.reply.skillshub.data.certificate.CertificateService;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.services.CvInformation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CertificateExtractorService {

  private final CertificateService certificateService;

  public List<HasCertificate> handleExtractedCertificates(List<CvInformation.Certificate> certificates) {
    List<HasCertificate> extractedCertificates = new ArrayList<>();
    for (var certificate : certificates) {
      var optionalCertificate = certificateService.findByNameIgnoreCase(certificate.getName());
      if (optionalCertificate.isEmpty()) {
        var newCertificate = new Certificate();
        newCertificate.setName(certificate.getName());
        newCertificate.setIssuer(certificate.getIssuer());
        certificateService.save(newCertificate);
        extractedCertificates.add(createHasCertificate(certificate.getIssued_date(), newCertificate));
      } else {
        extractedCertificates.add(createHasCertificate(certificate.getIssued_date(),optionalCertificate.get()));
      }
    }
    return extractedCertificates;
  }

  private HasCertificate createHasCertificate(String date, Certificate certificate) {
    var hasCertificate = new HasCertificate();
    hasCertificate.setCertificate(certificate);
    hasCertificate.setIssuedDate(parseDate(date));
    return hasCertificate;
  }

  private LocalDate parseDate(String date) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
      var temporal = formatter.parse(date);
      var year = temporal.get(ChronoField.YEAR);
      var month = temporal.get(ChronoField.MONTH_OF_YEAR);
      var parsed_date = LocalDate.of(year, month, 1);
      return parsed_date;
    } catch (Exception e) {
      return null;
    }
  }
  
}
