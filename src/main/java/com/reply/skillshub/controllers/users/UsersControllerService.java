package com.reply.skillshub.controllers.users;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersControllerService {

    private final CompanyService companyService;

    private final UserService userService;

    private final LoadCurrentUser loadCurrentUser;

    public CreatedUserResponse addNewUserToCompany(@NotEmpty String companyId, CreateUserRequest createUserRequest) {
        Company company = companyService
                            .findById(companyId)
                            .orElseThrow(() -> new NoCompanyFound());

        User currentUser = loadCurrentUser.loadSkillhubUserFromContext();

        if (company.getEmployees().stream().noneMatch(user -> user.getId() == currentUser.getId())) {
            throw new InsufficientRights();
        }

        User userToSave = createUserFromRequest(createUserRequest);

        userToSave.getCompanies().add(company);

        return createUserResponse(userService.save(userToSave), company);
    }

    private User createUserFromRequest(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setUserRole(UserRole.EMPLOYEE);
        return user;
    }

    private CreatedUserResponse createUserResponse(User user, Company company) {
        CreatedUserResponse response = new CreatedUserResponse();
        response.companyId(company.getId());
        response.email(user.getEmail());
        response.fullName(user.getFullname());
        response.id(user.getId());
        response.role(user.getUserRole().name());
        return response;
    }
    
}
