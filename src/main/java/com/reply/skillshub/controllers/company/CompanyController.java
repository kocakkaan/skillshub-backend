package com.reply.skillshub.controllers.company;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.CompanyApi;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;

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
    
}
