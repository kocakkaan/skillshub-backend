package com.reply.skillshub.controllers.company;

import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyRepository;
import com.reply.skillshub.data.user.User;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerServiceTest {

    @InjectMocks
    private CompanyControllerService companyControllerService;

    @Mock
    private LoadCurrentUser loadCurrentUser;

    @Mock
    private CompanyRepository companyRepository;
    

    @Test
    void testSuccessfullgetCompaniesForCurrentUser() {
        var user = Instancio.create(User.class);
        var companyList = Instancio.createList(Company.class);

        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        doReturn(companyList).when(companyRepository).findByEmployeesId(user.getId());

        var companiesDtos = companyControllerService.getCompaniesForCurrentUser();

        Assertions.assertEquals(companyList.size(), companiesDtos.size());

        for (int i = 0; i < companiesDtos.size(); i++) {
            var dto = companiesDtos.get(i);
            var entity = companyList.get(i);
            Assertions.assertEquals(entity.getId(), dto.getId());
            Assertions.assertEquals(dto.getName(), entity.getLabel());
        }        
    }

    @Test
    void throwsExceptionWhenNoCompaniesFound() {
        var user = Instancio.create(User.class);
        List<Company> companyList = List.of();

        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        doReturn(companyList).when(companyRepository).findByEmployeesId(user.getId());

        Assertions.assertThrows(NoCompanyFound.class, () -> companyControllerService.getCompaniesForCurrentUser());
    }
    
}
