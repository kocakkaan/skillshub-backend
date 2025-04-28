package com.reply.skillshub.controllers.certificate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.certificate.Certificate;
import com.reply.skillshub.data.certificate.CertificateService;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CertificateDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateControllerService {

    private final UserService userService;
    private final CertificateService certificateService;

    public List<HasCertificate> findAllByUserId(String userId) {
        return userService.findById(userId).getHasCertificates();
    }

    public void deleteCertificateForUser(String userId, String certificateId) {
        var user = userService.findById(userId, UserWithCertificates.class);
        user.getHasCertificates().removeIf(c -> c.getId().equals(Long.parseLong(certificateId)));
        userService.save(user);
    }

    public Certificate convertToCertificateAndSave(CertificateDto certificateDto) {
        Certificate certificate = new Certificate();
        certificate.setName(certificateDto.getName());
        certificate.setIssuer(certificateDto.getIssuer());
        return certificateService.save(certificate);
    }

    public HasCertificate saveCertificateForUser(String userId, CertificateDto certificateDto) throws UserNotFound {
        Certificate certificate = certificateService.findByName(certificateDto.getName()).orElse(convertToCertificateAndSave(certificateDto));
        var user = userService.findById(userId, UserWithCertificates.class);

        HasCertificate hasCertificate = new HasCertificate();
        hasCertificate.setCertificate(certificate);
        hasCertificate.setFile(certificateDto.getFile().orElse(null));
        hasCertificate.setIssuedDate(certificateDto.getIssuedDate());
        hasCertificate.setExpirationDate(certificateDto.getExpirationDate().orElse(null));

        user.getHasCertificates().add(hasCertificate);
        userService.save(user);

        return hasCertificate;
    }
}
