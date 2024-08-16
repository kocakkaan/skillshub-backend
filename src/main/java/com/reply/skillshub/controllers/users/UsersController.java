package com.reply.skillshub.controllers.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.UsersApi;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UsersControllerService usersControllerService;

    @Override
    public ResponseEntity<CreatedUserResponse> companyCompanyIdEmployeesPost(String companyId,
            CreateUserRequest createUserRequest) {
        return ResponseEntity.status(201).body(usersControllerService.addNewUserToCompany(companyId, createUserRequest));
    }
    
}
