package com.reply.skillshub.hascertificate;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.data.hascertificate.HasCertificateRepository;
import com.reply.skillshub.data.hascertificate.HasCertificateService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {HasCertificateService.class, ValidationHandler.class, TestConfiguration.class})
public class HasCertificateServiceTest {

    @Autowired
    private HasCertificateService hasCertificateService;

    @MockBean
    private HasCertificateRepository hasCertificateRepository;

    @Test
    void throwsError_whenNoIssuedDate() {
        HasCertificate hasCertificate = Instancio.create(HasCertificate.class);
        hasCertificate.setIssuedDate(null);

        Assertions.assertThrows(ValidationException.class, () -> hasCertificateService.save(hasCertificate));


    }

    @Test
    void successful_save(){
        HasCertificate hasCertificate = Instancio.create(HasCertificate.class);

        doReturn(hasCertificate).when(hasCertificateRepository).save(any(HasCertificate.class));

        HasCertificate hasCertificateOutput = hasCertificateService.save(hasCertificate);

        Assertions.assertEquals(hasCertificate.getCertificate().getIssuer(), hasCertificateOutput.getCertificate().getIssuer());
        Assertions.assertEquals(hasCertificate.getCertificate().getName(), hasCertificateOutput.getCertificate().getName());
        Assertions.assertEquals(hasCertificate.getExpirationDate(), hasCertificateOutput.getExpirationDate());
        Assertions.assertEquals(hasCertificate.getFile(), hasCertificateOutput.getFile());
        Assertions.assertEquals(hasCertificate.getIssuedDate(), hasCertificateOutput.getIssuedDate());
    }
}
