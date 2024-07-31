package com.reply.skillshub.base.authentification;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserRepository;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.openapi.api.AuthentificationApi;
import com.reply.skillshub.openapi.api.UserManagementApi;
import com.reply.skillshub.openapi.model.SignUpRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
public class AuthentificationController implements AuthentificationApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> authSignupPost(SignUpRequest signUpRequest) {

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
                // create user object
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getAccessCode()));
        user.setUserRole(UserRole.ADMIN);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
