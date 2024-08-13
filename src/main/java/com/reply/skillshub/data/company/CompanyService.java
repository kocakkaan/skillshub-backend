package com.reply.skillshub.data.company;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final ValidationHandler<Company> validationHandler;

    public Company save(Company company) {
        validationHandler.validate(company);
        return companyRepository.save(company);
    }

    public List<Company> findByEmployeesId(String id) {
        return companyRepository.findByEmployeesId(id);
    }

    
}
