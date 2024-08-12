package com.reply.skillshub.base.authentification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.AuthentificationApi;
import com.reply.skillshub.openapi.model.LoginRequest;
import com.reply.skillshub.openapi.model.LoginResponse;
import com.reply.skillshub.openapi.model.SignupRequest;

@RestController
public class AuthentificationController implements AuthentificationApi {

    private final AuthentificationService authentificationService;

    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @Override
    public ResponseEntity<String> authSignupPost(SignupRequest signupRequest) {
        authentificationService.signUpUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    public ResponseEntity<String> authConfirmationConfirmationTokenGet(String token) {
        authentificationService.confirmUser(token);
        return ResponseEntity.ok("User confirmed successfully");
    }

    @Override
    public ResponseEntity<LoginResponse> authLoginPost(LoginRequest loginRequest) {
        return ResponseEntity.ok(authentificationService.loginUser(loginRequest));
    }
}
