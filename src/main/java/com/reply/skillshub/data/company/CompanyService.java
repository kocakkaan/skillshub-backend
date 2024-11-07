package com.reply.skillshub.data.company;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final ValidationHandler<Company> validationHandler = new ValidationHandler<>();

    public Company save(Company company) {
        validationHandler.validate(company);
        return companyRepository.save(company);
    }

    public List<Company> findByEmployeesId(String id) {
        return companyRepository.findByEmployeesId(id);
    }

    public Optional<Company> findById(String id) {
        return companyRepository.findById(id);
    }
    
}
