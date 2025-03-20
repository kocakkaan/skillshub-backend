package com.reply.skillshub.data.certificate;

import com.reply.skillshub.base.services.ValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    private final ValidationHandler<Certificate> validationHandler = new ValidationHandler<>();

    public Certificate save(Certificate certificate) {
        validationHandler.validate(certificate);
        return certificateRepository.save(certificate);
    }

    public Optional<Certificate> findById(String id) {
        return certificateRepository.findById(id);
    }

    public Optional<Certificate> findByName(String name) {
        return certificateRepository.findByName(name);
    }

    public Optional<Certificate> findByNameIgnoreCase(String name) {
        return certificateRepository.findByNameIgnoreCase(name);
    }

}


