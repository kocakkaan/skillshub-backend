package com.reply.skillshub.data.company;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.reply.skillshub.TestConfiguration;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;
import com.reply.skillshub.data.user.User;

@SpringBootTest(classes = {CompanyService.class, ValidationHandler.class, TestConfiguration.class})
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @MockitoBean
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

    @Test
    void successfull_save() {
        Company company = new Company();
        company.setLabel("someLabel");
        company.getEmployees().add(Instancio.create(User.class));
        
        doReturn(company).when(companyRepository).save(any(Company.class));

        Company returnedCompany = companyService.save(company);

        Assertions.assertNotNull(returnedCompany);
    }
}
