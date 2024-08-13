package com.reply.skillshub.data.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest(classes = {CompanyService.class, ValidationHandler.class, TestConfiguration.class})
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @MockBean
    private CompanyRepository companyRepository;

    @Test
    void throwsError_whenNoLabel() {
        Company company = new Company();
        company.setLabel(null);
        Assertions.assertThrows(ValidationException.class, () -> companyService.save(company));
    }

    @Test
    void throwsError_whenNoUser() {
        Company company = new Company();
        company.setLabel("someLabel");
        Assertions.assertThrows(ValidationException.class, () -> companyService.save(company));
    }
}
