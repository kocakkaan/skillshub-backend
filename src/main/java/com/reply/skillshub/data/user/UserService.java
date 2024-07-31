package com.reply.skillshub.data.user;

import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository personRepository;
    private final Validator validator;

    public User save(User person) {
        Set<ConstraintViolation<User>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            // Handle validation errors (e.g., throw an exception, log errors, etc.)
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
        return personRepository.save(person);
    }
}
