package com.reply.skillshub.data.hascertificate;

import com.reply.skillshub.base.services.ValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HasCertificateService {

    private final HasCertificateRepository hasCertificateRepository;
    private final ValidationHandler<HasCertificate> validationHandler = new ValidationHandler<>();

    public HasCertificate save(HasCertificate hasCertificate) {
        validationHandler.validate(hasCertificate);
        return hasCertificateRepository.save(hasCertificate);
    }
}
