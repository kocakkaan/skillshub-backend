package com.reply.skillshub.data.certificate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;

@SpringBootTest(classes = {CertificateService.class, ValidationHandler.class, TestConfiguration.class})
public class CertificateServiceTest {
    @Autowired
    private CertificateService certificateService;

    @MockitoBean
    private CertificateRepository certificateRepository;

    @Test
    void throwsError_whenNoIssuerenNoName() {
        Certificate certificate = new Certificate();
        certificate.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> certificateService.save(certificate));
    }

    @Test
    void successful_save() {
        Certificate certificate = new Certificate();
        certificate.setIssuer("AWS");
        certificate.setName("Title");

        doReturn(certificate).when(certificateRepository).save(any(Certificate.class));

        Certificate certificateOutput = certificateService.save(certificate);

        Assertions.assertNotNull(certificateOutput);
    }

    @Test
    void findCertificatebyName() {
        Certificate certificate = new Certificate();
        certificate.setIssuer("AWS");
        certificate.setName("Title");

        doReturn(Optional.of(certificate)).when(certificateRepository).findByName(any(String.class));

        Certificate certificateOutput = certificateService.findByName("Title").orElse(null);
        Assertions.assertNotNull(certificateOutput);
        Assertions.assertEquals(certificate.getIssuer(), certificateOutput.getIssuer());
        Assertions.assertEquals(certificate.getName(), certificateOutput.getName());
    }

    @Test
    void findCertificateById() {
        Certificate certificate = new Certificate();
        certificate.setIssuer("AWS");
        certificate.setName("Title");

        doReturn(Optional.of(certificate)).when(certificateRepository).findById(any(String.class));

        Certificate certificateOutput = certificateService.findById("an id").orElse(null);
        Assertions.assertNotNull(certificateOutput);
        Assertions.assertEquals(certificate.getIssuer(), certificateOutput.getIssuer());
        Assertions.assertEquals(certificate.getName(), certificateOutput.getName());

    }

}
