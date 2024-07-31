package com.reply.skillshub.base.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.user.UserRepository;
import com.reply.skillshub.openapi.model.SignUpRequest;

@Service
public class AuthentificationService {

    @Autowired
    private UserRepository userRepository;

    public void signUpUser(SignUpRequest signUpRequest) {
        
    }

    
}
