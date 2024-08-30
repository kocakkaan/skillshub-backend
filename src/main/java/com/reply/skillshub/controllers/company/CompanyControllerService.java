package com.reply.skillshub.controllers.company;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyControllerService {

    private final LoadCurrentUser loadCurrentUser;

    private final CompanyService companyService;

    public List<CompanyDto> getCompaniesForCurrentUser() {
        var user = loadCurrentUser.loadSkillhubUserFromContext();
        var companies = companyService.findByEmployeesId(user.getId());

        if (companies.isEmpty()) {
            throw new NoCompanyFound("The current user has no related companies.");
        }

        return companies.stream().map(this::convertEntityToApiDto).toList();
    }

    private CompanyDto convertEntityToApiDto(Company company) {
        return new CompanyDto()
            .id(company.getId())
            .name(company.getLabel());
    }

    public CompanyDto createCompany(CreateCompanyRequest createCompanyRequest) {
        var company = new Company();
        var user = loadCurrentUser.loadSkillhubUserFromContext();
        company.setLabel(createCompanyRequest.getCompanyName());
        company.getEmployees().add(user);
        return convertEntityToApiDto(companyService.save(company));
    }
    
}
