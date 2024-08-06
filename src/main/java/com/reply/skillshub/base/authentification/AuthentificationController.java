package com.reply.skillshub.base.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.AuthentificationApi;
import com.reply.skillshub.openapi.model.SignUpRequest;

@RestController
public class AuthentificationController implements AuthentificationApi {

    @Autowired
    private AuthentificationService authentificationService;

    @Override
    public ResponseEntity<String> authSignupPost(SignUpRequest signUpRequest) {
        authentificationService.signUpUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    public ResponseEntity<String> authConfirmationConfirmationTokenGet(String token) {
        authentificationService.confirmUser(token);
        return ResponseEntity.ok("User confirmed successfully");
    }
}
