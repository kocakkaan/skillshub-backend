package com.reply.skillshub.controllers.company;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.CompanyApi;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CompanyInformationDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;
import com.reply.skillshub.openapi.model.EmployeeDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyApi {

    private final CompanyControllerService companyControllerService;

    @Override
    public ResponseEntity<List<CompanyDto>> userMeCompanyGet() {
        return ResponseEntity.ok(companyControllerService.getCompaniesForCurrentUser());
    }

    @Override
    public ResponseEntity<CompanyDto> userMeCompanyPost(CreateCompanyRequest createCompanyRequest) {
        return ResponseEntity.ok(companyControllerService.createCompany(createCompanyRequest));
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> companyCompanyIdEmployeesGet(String companyId, Optional<String> searchString) {
        return ResponseEntity.ok(companyControllerService.getCompanyEmployees(companyId, searchString));
    }

    @Override
    public ResponseEntity<CompanyInformationDto> companyCompanyIdGet(String companyId) {
        return ResponseEntity.ok(companyControllerService.getCompanyInformation(companyId));
    }
    
}
