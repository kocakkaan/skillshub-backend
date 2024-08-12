package com.reply.skillshub.controllers.company;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyControllerService {

    // private final CompanyRepository companyRepository;

    public List<com.reply.skillshub.openapi.model.Company> getCompaniesForCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return List.of();
        
    }

    private com.reply.skillshub.openapi.model.Company convertEntityToApiDto(Company company) {
        return new com.reply.skillshub.openapi.model.Company()
            .id(company.getId())
            .name(company.getLabel());
    }
    
}
