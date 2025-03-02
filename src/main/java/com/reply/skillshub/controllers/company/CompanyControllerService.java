package com.reply.skillshub.controllers.company;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.BaseCompany;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.user.Employee;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CompanyInformationDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;
import com.reply.skillshub.openapi.model.EmployeeDto;
import com.reply.skillshub.services.SkillsAgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyControllerService {

    private final LoadCurrentUser loadCurrentUser;

    private final CompanyService companyService;

    private final UserService userService;

    private final SkillsAgentService skillsAgentService;

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
        var baseUser = loadCurrentUser.loadSkillhubUserFromContext();
        var user = userService.findById(baseUser.getId());
        company.setLabel(createCompanyRequest.getCompanyName());
        company.getEmployees().add(user);
        return convertEntityToApiDto(companyService.save(company));
    }

    public CompanyInformationDto getCompanyInformation(String companyId) {
        var company = companyService.findBaseCompanyById(companyId).orElseThrow(NoCompanyFound::new);
        return new CompanyInformationDto()
            .id(company.getId())
            .name(company.getLabel())
            .employees(company.getEmployees().stream().map(this::convertEmployeeToApiDto).toList());
    }

    public List<EmployeeDto> getCompanyEmployees(String companyId, Optional<String> searchString) {
        var company = companyService.findBaseCompanyById(companyId).orElseThrow(NoCompanyFound::new);
        userService.findByCompaniesIdIn(List.of(companyId));
        if (searchString.isPresent()) {
            var keywords = skillsAgentService.getKeywordsFromSearchString(searchString.get());
            return userService.findByCompanyAndKeyWords(companyId, keywords).stream()
                .map(this::convertEmployeeToApiDto)
                .toList();
        }
        return company.getEmployees().stream().map(this::convertEmployeeToApiDto).toList();
    }

    private EmployeeDto convertEmployeeToApiDto(BaseUser employee) {
        return new EmployeeDto()
            .id(employee.getId())
            .fullname(employee.getFullName())
            .company("TODO: Implement company name");
    }

    private EmployeeDto convertEmployeeToApiDto(Employee employee) {
        return new EmployeeDto()
            .id(employee.getId())
            .fullname(employee.getFullName())
            .company("TODO: Implement company name");
    }
    
}
