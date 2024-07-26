package com.reply.skillshub.data.person;

import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
    
    private final PersonRepository personRepository;
    private final Validator validator;

    Person save(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            // Handle validation errors (e.g., throw an exception, log errors, etc.)
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
        return personRepository.save(person);
    }
}
