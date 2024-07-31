package com.reply.skillshub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.UserManagementApi;
import com.reply.skillshub.openapi.model.SignUpRequest;

import jakarta.validation.Valid;

@RestController
public class UserManagementController implements UserManagementApi {
    
    @Override
    public ResponseEntity<String> userSignupPost(SignUpRequest signUpRequest) {
        // TODO Auto-generated method stub
        return null;
    }
}
