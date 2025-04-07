package com.reply.skillshub.controllers.certificate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.certificate.Certificate;
import com.reply.skillshub.data.certificate.CertificateService;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CertificateDto;

@ExtendWith(MockitoExtension.class)
public class CertificateControllerServiceTest {

    @InjectMocks
    private CertificateControllerService certificateControllerService;

    @Mock
    private CertificateService certificateService;

    @Mock
    private UserService userService;

    @Test
    void testFindAllByUserId() {
        List<HasCertificate> hasCertificateList = Instancio.createList(HasCertificate.class);
        User user = Instancio.create(User.class);
        user.setHasCertificates(hasCertificateList);

        doReturn(user).when(userService).findById(user.getId());

        List<HasCertificate> hasCertificatesListOutput = certificateControllerService.findAllByUserId(user.getId());

        Assertions.assertEquals(user.getHasCertificates().size(), hasCertificatesListOutput.size());

        for (int i = 0; i < hasCertificateList.size(); i++) {
            HasCertificate output = hasCertificatesListOutput.get(i);
            HasCertificate hasCertificate = user.getHasCertificates().get(i);

            Assertions.assertEquals(output.getId(), hasCertificate.getId());
            Assertions.assertEquals(output.getFile(), hasCertificate.getFile());
            Assertions.assertEquals(output.getIssuedDate(), hasCertificate.getIssuedDate());
            Assertions.assertEquals(output.getExpirationDate(), hasCertificate.getExpirationDate());
            Assertions.assertEquals(output.getCertificate().getName(), hasCertificate.getCertificate().getName());
            Assertions.assertEquals(output.getCertificate().getIssuer(), hasCertificate.getCertificate().getIssuer());
        }
    }

    @Test
    void test_successSaveCertificateForUser() {
        UserWithCertificates user = Instancio.create(UserWithCertificates.class);
        Certificate certificate = Instancio.create(Certificate.class);
        HasCertificate hasCertificate = new HasCertificate();
        CertificateDto certificateDto = Instancio.create(CertificateDto.class);
        certificateDto.getFile().ifPresent((value) -> hasCertificate.setFile(value));
        hasCertificate.setCertificate(certificate);
        hasCertificate.setIssuedDate(certificateDto.getIssuedDate());
        certificateDto.getExpirationDate().ifPresent((value) -> hasCertificate.setExpirationDate(value));

        doReturn(Optional.of(certificate)).when(certificateService).findByName(certificateDto.getName());
        doReturn(user).when(userService).findById(user.getId(), UserWithCertificates.class);

        HasCertificate hasCertificateOutput = certificateControllerService.saveCertificateForUser(user.getId(), certificateDto);

        Assertions.assertEquals(hasCertificate.getFile(), hasCertificateOutput.getFile());
        Assertions.assertEquals(hasCertificate.getIssuedDate(), hasCertificateOutput.getIssuedDate());
        Assertions.assertEquals(hasCertificate.getExpirationDate(), hasCertificateOutput.getExpirationDate());
        Assertions.assertEquals(hasCertificate.getCertificate().getIssuer(), hasCertificateOutput.getCertificate().getIssuer());
        Assertions.assertEquals(hasCertificate.getCertificate().getName(), hasCertificateOutput.getCertificate().getName());
    }

    @Test
    void test_throwsErrorSaveCertificateForUser() {
        Certificate certificate = Instancio.create(Certificate.class);
        CertificateDto certificateDto = Instancio.create(CertificateDto.class);

        doReturn(Optional.of(certificate)).when(certificateService).findByName(certificateDto.getName());
        doThrow(UserNotFound.class).when(userService).findById("falseId", UserWithCertificates.class);
        
        Assertions.assertThrows(UserNotFound.class, () -> certificateControllerService.saveCertificateForUser("falseId", certificateDto));
    }

    @Test
    void test_convertToCertificateAndSave() {
        CertificateDto certificateDto = Instancio.create(CertificateDto.class);
        Certificate certificate = new Certificate();

        certificate.setName(certificateDto.getName());
        certificate.setIssuer(certificateDto.getIssuer());

        doReturn(certificate).when(certificateService).save(any(Certificate.class));

        Certificate certificateOutput = certificateControllerService.convertToCertificateAndSave(certificateDto);

        Assertions.assertEquals(certificate.getName(), certificateOutput.getName());
        Assertions.assertEquals(certificate.getIssuer(), certificateOutput.getIssuer());
    }
}
