package com.reply.skillshub.controllers.company;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.BaseCompany;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CompanyInformationDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;
import com.reply.skillshub.openapi.model.EmployeeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyControllerService {

    private final LoadCurrentUser loadCurrentUser;

    private final CompanyService companyService;

    public List<CompanyDto> getCompaniesForCurrentUser() {
        var user = loadCurrentUser.loadSkillhubUserFromContext();
        var companies = companyService.findBaseCompanyByEmployeesId(user.getId());

        if (companies.isEmpty()) {
            throw new NoCompanyFound("The current user has no related companies.");
        }

        return companies.stream().map(this::convertEntityToApiDto).toList();
    }

    private CompanyDto convertEntityToApiDto(BaseCompany company) {
        return new CompanyDto()
            .id(company.getId())
            .name(company.getLabel());
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

    public CompanyInformationDto getCompanyInformation(String companyId) {
        var company = companyService.findById(companyId).orElseThrow(NoCompanyFound::new);
        return new CompanyInformationDto()
            .id(company.getId())
            .name(company.getLabel())
            .employees(company.getEmployees().stream().map(this::convertEmployeeToApiDto).toList());
    }

    private EmployeeDto convertEmployeeToApiDto(User employee) {
        return new EmployeeDto()
            .id(employee.getId())
            .fullname(employee.getFullname())
            .company(employee.getCompanies().get(0).getLabel());
    }
    
}
