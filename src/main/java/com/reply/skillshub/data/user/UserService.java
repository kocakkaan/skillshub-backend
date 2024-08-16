package com.reply.skillshub.data.user;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final Validator validator;

    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            // Handle validation errors (e.g., throw an exception, log errors, etc.)
            throw new IllegalArgumentException("Validation failed: " + violations);
        }

        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email is already taken");
        }

        return userRepository.save(user);
    }

    public Optional<User> findUserByConfirmationToken(String token) {
        return userRepository.findByConfirmationToken(token);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}
