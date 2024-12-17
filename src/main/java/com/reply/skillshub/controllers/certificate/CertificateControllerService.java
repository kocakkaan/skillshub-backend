package com.reply.skillshub.controllers.certificate;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.certificate.Certificate;
import com.reply.skillshub.data.certificate.CertificateService;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.data.hascertificate.HasCertificateService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CertificateDto;

import java.util.List;

public class CertificateControllerService {

    UserService userService;
    CertificateService certificateService;
    HasCertificateService hasCertificateService;

    public List<HasCertificate> findAllByUserId(String userId) {
        return userService.findById(userId).getHasCertificates();
    }

    public Certificate convertToCertificateAndSave(CertificateDto certificateDto){
        Certificate certificate = new Certificate();
        certificate.setName(certificateDto.getName());
        certificate.setIssuer(certificateDto.getIssuer());
        return certificateService.save(certificate);
    }

    public HasCertificate saveCertificateForUser(String userId, CertificateDto certificateDto) throws UserNotFound{
        Certificate certificate = certificateService.findByName(certificateDto.getName()).orElse(convertToCertificateAndSave(certificateDto));
        User user = userService.findUserById(userId).orElseThrow(UserNotFound::new);

        HasCertificate hasCertificate = new HasCertificate();
        hasCertificate.setCertificate(certificate);
        hasCertificate.setFile(certificateDto.getFile());
        hasCertificate.setIssuedDate(certificateDto.getIssuedDate());
        hasCertificate.setExpirationDate(certificateDto.getExpirationDate());

        HasCertificate savedHasCertificate = hasCertificateService.save(hasCertificate);
        user.getHasCertificates().add(savedHasCertificate);
        userService.save(user);

        return savedHasCertificate;
    }
}
