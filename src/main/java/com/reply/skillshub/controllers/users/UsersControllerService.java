package com.reply.skillshub.controllers.users;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidConfirmationToken;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.EmailRequest;
import com.reply.skillshub.data.certificate.Certificate;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.openapi.model.CertificateDto;
import com.reply.skillshub.openapi.model.ConfirmedUserResponse;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;
import com.reply.skillshub.openapi.model.EmployeeDto;
import com.reply.skillshub.openapi.model.ExperienceDto;
import com.reply.skillshub.openapi.model.LanguageDto;
import com.reply.skillshub.openapi.model.ProfileDto;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.UserConfirmRequest;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersControllerService {

    private final CompanyService companyService;

    private final UserService userService;

    private final LoadCurrentUser loadCurrentUser;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public ConfirmedUserResponse confirmUser(String userId, String confirmationToken, @Valid UserConfirmRequest userConfirmRequest) {
        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFound());
        
        if (!user.getConfirmationToken().equals(confirmationToken)) {
            throw new InvalidConfirmationToken();
        }

        user.setPassword(passwordEncoder.encode(userConfirmRequest.getPassword()));
        user.setConfirmed(true);

        return createConfirmedUserResponse(userService.save(user));
    }

    private ConfirmedUserResponse createConfirmedUserResponse(User user) {
        ConfirmedUserResponse response = new ConfirmedUserResponse();
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullname());
        response.setId(user.getId());
        response.setRole(user.getUserRole().name());
        return response;
    }

    public CreatedUserResponse addNewUserToCompany(@NotEmpty String companyId, CreateUserRequest createUserRequest) {
        Company company = companyService
                            .findById(companyId)
                            .orElseThrow(() -> new NoCompanyFound());

        User currentUser = loadCurrentUser.loadSkillhubUserFromContext();

        if (company.getEmployees().stream().noneMatch(user -> user.getId().equals(currentUser.getId()))) {
            throw new InsufficientRights();
        }

        User userToSave = createUserFromRequest(createUserRequest);

        userToSave.getCompanies().add(company);

        User savedUser = userService.save(userToSave);

        emailService.sendEmail(createNewUserEmailRequest(savedUser.getEmail()));

        return createUserResponse(savedUser, company);
    }

    public List<EmployeeDto> getEmployeesAccessibleToCurrentUser() {
        User currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        return getEmployeesAccessibleToUser(currentUser);
    }

    public ProfileDto getProfileForCurrentUser() {
        return getProfileForUser(loadCurrentUser.loadSkillhubUserFromContext());
    }

    public ProfileDto getProfileForEmployee(String employeeId) {
        return getProfileForUser(userService.findById(employeeId));
    }

    private ProfileDto getProfileForUser(User user) {
        var profile = new ProfileDto();
        profile.setFullname(user.getFullname());
        profile.setEmail(user.getEmail());
        profile.setId(user.getId());
        profile.setCertificates(user.getCertificates().stream().map(this::convertToCertificateDto).toList());
        profile.setExperiences(user.getExperiences().stream().map(this::convertToExperienceDto).toList());
        profile.setSkills(user.getSkills().stream().map(this::convertToSkillDto).toList());
        profile.setLanguages(user.getSpeaks().stream().map(this::convertSpeaksToLanguageDto).toList());
        return profile;
    }

    private LanguageDto convertSpeaksToLanguageDto(Speaks speaks) {
        var language = speaks.getLanguage();
        return new LanguageDto(language.getLanguageCode().getName(), language.getLanguageAlpha3Code().getName());
    }

    private SkillDto convertToSkillDto(Skill skill) {
        var skillDto = new SkillDto(skill.getId(), skill.getLabel());
        return skillDto;
    }

    private CertificateDto convertToCertificateDto(Certificate certificate) {
        return new CertificateDto(certificate.getId(), certificate.getLabel());
    }

    private ExperienceDto convertToExperienceDto(Experience experience) {
        var experienceDto = new ExperienceDto();
        experienceDto.setTitle(experience.getTitle());
        experienceDto.setResponsibilities(experience.getDescriptions());
        return experienceDto;
    }

    public List<EmployeeDto> getEmployeeAccessibleToUserWithId(String userId) {
        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFound());
        return getEmployeesAccessibleToUser(user);
    }

    private List<EmployeeDto> getEmployeesAccessibleToUser(User user) {
        List<String> companyIds = user.getCompanies().stream().map(Company::getId).toList();
        return userService.findByCompaniesIdIn(companyIds).stream().map(this::convertUserToEmployeeDto).toList();
    }

    private EmployeeDto convertUserToEmployeeDto(User user) {
        return new EmployeeDto()
            .id(user.getId())
            .fullname(user.getFullname())
            .role(user.getUserRole().name());
    }

    private User createUserFromRequest(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setUserRole(UserRole.EMPLOYEE);
        user.setPassword("tempPassword");
        return user;
    }

    private CreatedUserResponse createUserResponse(User user, Company company) {
        CreatedUserResponse response = new CreatedUserResponse();
        response.companyId(company.getId());
        response.email(user.getEmail());
        response.fullName(user.getFullname());
        response.id(user.getId());
        response.role(user.getUserRole().name());
        return response;
    }

    private EmailRequest createNewUserEmailRequest(String userEmail) {
        EmailRequest email = new EmailRequest();
        email.setRecipient(userEmail);
        email.setMessage(createNewUserMessage());
        email.setSubject("An account has been created for you");
        return email;
    }

    private String createNewUserMessage() {
        StringBuilder message = new StringBuilder();
        message.append("An account has been created for you.");
        message.append("Please use this link to set your password");
        message.append("someLink");

        return message.toString();
    }
    
}
