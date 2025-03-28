package com.reply.skillshub.controllers.company;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reply.skillshub.BaseCompanyImp;
import com.reply.skillshub.BaseUserImp;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerServiceTest {

    @InjectMocks
    private CompanyControllerService companyControllerService;

    @Mock
    private LoadCurrentUser loadCurrentUser;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;
    

    @Test
    void testSuccessfullgetCompaniesForCurrentUser() {
        var user = Instancio.create(BaseUserImp.class);
        var companyList = Instancio.ofList(BaseCompanyImp.class).size(2).create();


        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        doReturn(companyList).when(companyService).findBaseCompanyByEmployeesId(user.getId());

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
        var user = Instancio.create(BaseUserImp.class);
        List<Company> companyList = List.of();

        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        doReturn(companyList).when(companyService).findBaseCompanyByEmployeesId(user.getId());

        Assertions.assertThrows(NoCompanyFound.class, () -> companyControllerService.getCompaniesForCurrentUser());
    }

    @Test
    void createCompany_returnsCorrectObjectAfterSave() {
        var user = Instancio.create(BaseUserImp.class);
        var user1 = Instancio.create(User.class);
        user1.setId(user.getId());
        CreateCompanyRequest request = Instancio.create(CreateCompanyRequest.class);
        
        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        doReturn(user1).when(userService).findById(user.getId());
        doAnswer(invocation -> invocation.getArgument(0)).when(companyService).save(any(Company.class));
        
        CompanyDto company = companyControllerService.createCompany(request);

        Assertions.assertEquals(request.getCompanyName(), company.getName());
    }

    
}
