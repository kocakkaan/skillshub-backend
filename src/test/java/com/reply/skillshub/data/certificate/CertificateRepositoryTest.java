package com.reply.skillshub.data.certificate;

import com.reply.skillshub.BaseRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

@DataNeo4jTest
public class CertificateRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @Test
    void test_succesfullySaveCertificate() {
        Certificate certificate = new Certificate();
        certificate.setName("New certificate name");
        certificate.setIssuer("AWS");
        Certificate savedCertificate = certificateRepository.save(certificate);
        Assertions.assertNotNull(savedCertificate.getId());
    }

    @Test
    void test_findCertificateById() {
        Certificate certificate = new Certificate();
        certificate.setName("New certificate name");
        certificate.setIssuer("AWS");
        certificateRepository.save(certificate);

        Certificate certificateOutput = certificateRepository.findByName("New certificate name").orElse(null);
        Assertions.assertNotNull(certificateOutput);
        Assertions.assertEquals(certificateOutput.getName(), certificate.getName());
        Assertions.assertEquals(certificateOutput.getIssuer(), certificate.getIssuer());
    }

    @Test
    void test_findCertificateByName() {
        Certificate certificate = new Certificate();
        certificate.setName("New certificate name");
        certificate.setIssuer("AWS");
        certificateRepository.save(certificate);

        Certificate certificateOutput = certificateRepository.findByName("New certificate name").orElse(null);
        Assertions.assertNotNull(certificateOutput);
        Assertions.assertEquals(certificateOutput.getName(), certificate.getName());
        Assertions.assertEquals(certificateOutput.getIssuer(), certificate.getIssuer());
    }
}
