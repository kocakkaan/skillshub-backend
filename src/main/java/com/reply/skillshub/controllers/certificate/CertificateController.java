package com.reply.skillshub.controllers.certificate;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.openapi.api.CertificatesApi;
import com.reply.skillshub.openapi.model.CertificateDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CertificateController implements CertificatesApi {

    private final CertificateControllerService certificateControllerService;

    @Override
    public ResponseEntity<List<CertificateDto>> usersUserIdCertificatesGet(String userId) {
        List<CertificateDto> certificateDtos = certificateControllerService.findAllByUserId(userId).stream().map(this::convertToCertificateDto).toList();
        return ResponseEntity.ok(certificateDtos);
    }

    @Override
    public ResponseEntity<CertificateDto> usersUserIdCertificatesPost(String userId, @Valid CertificateDto certificateDto) {
        CertificateDto newCertificateDto;

        try {
            HasCertificate savedHasCertificate = certificateControllerService.saveCertificateForUser(userId, certificateDto);
            newCertificateDto = convertToCertificateDto(savedHasCertificate);
        } catch (UserNotFound userNotFound) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(newCertificateDto);
    }

    private CertificateDto convertToCertificateDto(HasCertificate hasCertificate) {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(hasCertificate.getCertificate().getId());
        certificateDto.setIssuer(hasCertificate.getCertificate().getIssuer());
        certificateDto.setExpirationDate(Optional.ofNullable(hasCertificate.getExpirationDate()));
        certificateDto.setIssuedDate(hasCertificate.getIssuedDate());
        certificateDto.setFile(Optional.ofNullable(hasCertificate.getFile()));
        return certificateDto;
    }

    @Override
    public ResponseEntity<Void> usersUserIdCertificatesDelete(String userId, @NotNull @Valid String certificateId) {
        certificateControllerService.deleteCertificateForUser(userId, certificateId);
        return ResponseEntity.noContent().build();
    }
}
