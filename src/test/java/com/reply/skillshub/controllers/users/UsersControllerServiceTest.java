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
import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidConfirmationToken;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.ConfirmedUserResponse;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;
import com.reply.skillshub.openapi.model.UserConfirmRequest;

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

    @Mock
    private EmailService emailService;

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

    @Test
    void userNotFound_confirmUser() {
        doReturn(Optional.empty()).when(userService).findUserById(anyString());
        Assertions.assertThrows(UserNotFound.class, () -> usersControllerService.confirmUser("someId", null, null));
    }

    @Test
    void invalidToken_confirmUser() {
        User user = Instancio.create(User.class);
        doReturn(Optional.of(user)).when(userService).findUserById(user.getId());
        Assertions.assertThrows(InvalidConfirmationToken.class, () -> usersControllerService.confirmUser(user.getId(), "false", null));
    }

    @Test
    void successful_confirmUser() {
        User user = Instancio.create(User.class);
        UserConfirmRequest request = Instancio.create(UserConfirmRequest.class);

        doReturn(Optional.of(user)).when(userService).findUserById(user.getId());
        doAnswer(invocation -> invocation.getArgument(0)).when(userService).save(any(User.class));

        ConfirmedUserResponse response = usersControllerService.confirmUser(user.getId(), user.getConfirmationToken(), request);
        
        Assertions.assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void successful_getProfileForCurrentUser() {
        User user = Instancio.create(User.class);
        doReturn(user).when(loadCurrentUser).loadSkillhubUserFromContext();
        var profile = usersControllerService.getProfileForCurrentUser();
        Assertions.assertNotNull(profile);
        Assertions.assertEquals(user.getResumes().size(), profile.getResumes().size());
        Assertions.assertEquals(user.getSkills().size(), profile.getSkills().size());
        Assertions.assertEquals(user.getExperiences().size(), profile.getExperiences().size());
        Assertions.assertEquals(user.getHasCertificates().size(), profile.getCertificates().size());
        // Assertions.assertEquals(user.getEducations().size(), profile.getEducations().size());
    }
}
