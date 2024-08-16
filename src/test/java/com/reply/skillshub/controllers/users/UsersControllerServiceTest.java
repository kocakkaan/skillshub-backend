package com.reply.skillshub.controllers.users;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;

@ExtendWith(MockitoExtension.class)
public class UsersControllerServiceTest {
    
    @InjectMocks
    private UsersControllerService usersControllerService;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;

    @Mock
    private LoadCurrentUser loadCurrentUser;

    @Test
    void addNewUserNoCompanyFound() {
        doReturn(Optional.empty()).when(companyService).findById(anyString());

        Assertions
            .assertThrows(
                NoCompanyFound.class, 
                () -> usersControllerService.addNewUserToCompany("123", Instancio.create(CreateUserRequest.class)));
    }

    @Test
    void addNewUserInsuffientRights() {
        doReturn(Optional.of(Instancio.create(Company.class))).when(companyService).findById(anyString());
        doReturn(Instancio.create(User.class)).when(loadCurrentUser).loadSkillhubUserFromContext();
        Assertions
            .assertThrows(
                InsufficientRights.class, 
                () -> usersControllerService.addNewUserToCompany("123", Instancio.create(CreateUserRequest.class)));
    }

    @Test
    void succesfullUserCreation() {
        Company company = Instancio.create(Company.class);
        User adminUser = Instancio.create(User.class);
        CreateUserRequest request = Instancio.create(CreateUserRequest.class);

        company.getEmployees().add(adminUser);

        doReturn(Optional.of(company)).when(companyService).findById(company.getId());
        doReturn(adminUser).when(loadCurrentUser).loadSkillhubUserFromContext();
        doAnswer(invocation -> invocation.getArgument(0)).when(userService).save(any(User.class));

        CreatedUserResponse response = usersControllerService.addNewUserToCompany(company.getId(), request);

        Assertions.assertEquals(request.getEmail(), response.getEmail());
        Assertions.assertEquals(request.getFirstName() + " " + request.getLastName(), response.getFullName());
        Assertions.assertNotNull(response.getRole());
        Assertions.assertEquals(company.getId(), response.getCompanyId());
    }
}
