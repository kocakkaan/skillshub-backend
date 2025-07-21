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
import com.reply.skillshub.services.LinkGeneratorService;
import com.reply.skillshub.services.SkillsAgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyControllerService {

    private final LoadCurrentUser loadCurrentUser;

    private final CompanyService companyService;

    private final UserService userService;

    private final LinkGeneratorService linkGeneratorService;

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
        final String companyName = company.getLabel();
        return new CompanyInformationDto()
                .id(company.getId())
                .name(company.getLabel())
                .employees(company.getEmployees().stream()
                        .map(employee -> convertEmployeeToApiDto(employee, companyName)).toList());
    }

    public List<EmployeeDto> getCompanyEmployees(String companyId, Optional<String> searchString) {
        var company = companyService.findBaseCompanyById(companyId).orElseThrow(NoCompanyFound::new);
        final String companyName = company.getLabel();
        
        if (searchString.isPresent() && !searchString.get().isBlank()) {
            // Get matching employee IDs directly from agent service
            List<String> matchingEmployeeIds = skillsAgentService.getMatchingEmployeeIds(
                searchString.get(), 
                List.of(companyId)
            );
            
            // Fetch employees by IDs
            List<Employee> employees = userService.findEmployeesByIds(matchingEmployeeIds);
            
            return employees.stream()
                    .map(employee -> convertEmployeeToApiDto(employee, companyName))
                    .toList();
        }
        return company.getEmployees().stream().map(employee -> convertEmployeeToApiDto(employee, companyName)).toList();
    }

    private EmployeeDto convertEmployeeToApiDto(BaseUser employee, String companyName) {
        return new EmployeeDto()
                .id(employee.getId())
                .fullname(employee.getFullName())
                .confirmed(employee.getConfirmed() != null ? employee.getConfirmed() : false)
                .confirmationLink(linkGeneratorService.getConfirmationLink(employee))
                .createdBy(employee.getCreatedBy() != null ? employee.getCreatedBy().getFullName() : null)
                .createdOn(employee.getCreatedOn() != null ? employee.getCreatedOn() : null)
                .company(companyName);
    }

    private EmployeeDto convertEmployeeToApiDto(Employee employee, String companyName) {
        return new EmployeeDto()
                .id(employee.getId())
                .fullname(employee.getFullName())
                .createdBy(employee.getCreatedBy().getFullName())
                .confirmed(employee.getConfirmed())
                .confirmationLink(linkGeneratorService.getConfirmationLink(employee))
                .createdOn(employee.getCreatedOn())
                .company(companyName);
    }

}
