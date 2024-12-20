package com.reply.skillshub.data.industry;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    private final ValidationHandler<Industry> validationHandler = new ValidationHandler<>();

    public Industry save(Industry company) {
        validationHandler.validate(company);
        return industryRepository.save(company);
    }

    public Optional<Industry> findById(String id) {
        return industryRepository.findById(id);
    }

    public List<Industry> findAllIndustries() {
        return industryRepository.findAll();
    }

    public List<Industry> findByLabel(String label) {
        return industryRepository.findByLabel(label);
    }

    public List<Industry> findAllByResumeId(String resumeId) {
        return industryRepository.findAllByResumeId(resumeId);
    }
    
}
