package com.reply.skillshub.data.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.data.userrole.UserRole;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final Validator validator;
    private final Neo4jTemplate neo4jTemplate;

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

    public <T> Optional<T> findUserByConfirmationToken(String token, Class<T> userType) {
        return userRepository.findByConfirmationToken(token, userType);
    }

    public Optional<BaseUser> findBaseUserByEmail(String email) {
        return userRepository.findByEmail(email, BaseUser.class);
    }

    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFound());
    }

    public EmployeeProfile findEmployeeProfileById(String id) {
        return userRepository.findById(id, EmployeeProfile.class).orElseThrow(() -> new UserNotFound());
    }
    
    public List<Employee> findByCompaniesIdIn(List<String> company) {
        return userRepository.findByCompaniesIdIn(company);
    }

    public List<Employee> findByCompanyAndKeyWords(String companyId, List<String> keywordsInput) {
        var keywords = keywordsInput.stream().map(String::toLowerCase).toList();
        return userRepository.findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn(companyId, keywords, keywords);
    }

    public List<Employee> findByCompaniesAndKeyWords(List<String> companyId, List<String> keywordsInput) {
        var keywords = keywordsInput.stream().map(String::toLowerCase).toList();
        return userRepository.findByCompaniesIdInAndSkillsLabelInOrHasCertificatesCertificateNameIn(companyId, keywords, keywords);
    }

    public List<Employee> findEmployeesByIds(List<String> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return employeeIds.stream()
            .map(id -> {
                try {
                    return findById(id, Employee.class);
                } catch (UserNotFound e) {
                    //logger.debug("User with id {} not found", id);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Returns the first User with the given role.
     * Used by the signup flow to locate the super/root admin that serves
     * as the {@code createdBy} reference for newly registered users.
     */
    public Optional<User> findFirstByUserRole(UserRole role) {
        return userRepository.findFirstByUserRole(role);
    }

    public BaseUser findByResumeId(String id) {
        return userRepository.findByResumesId(id, BaseUser.class);
    }

    public <T> T findById(String id, Class<T> userType) throws UserNotFound {
        return userRepository.findById(id, userType).orElseThrow(() -> new UserNotFound());
    }

    // T needs to have something in common with User.class
    public <T> T save(T user) {
        return neo4jTemplate.save(User.class).one(user);
    }
}
