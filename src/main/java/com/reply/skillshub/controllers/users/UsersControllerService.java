package com.reply.skillshub.controllers.users;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidConfirmationToken;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProfilePictureNotSavedException;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.base.services.LoadCurrentUser;
import com.reply.skillshub.data.EmailRequest;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.company.CompanyService;
import com.reply.skillshub.data.company.MinimalCompany;
import com.reply.skillshub.data.skill.SkillService;
import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.user.Employee;
import com.reply.skillshub.data.user.EmployeeProfile;
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
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.ProfileDto;
import com.reply.skillshub.openapi.model.ProfileDtoResumesInner;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.UserConfirmRequest;
import com.reply.skillshub.services.SkillsAgentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersControllerService {

    private final CompanyService companyService;

    private final UserService userService;

    private final SkillService skillService;

    private final LoadCurrentUser loadCurrentUser;

    private final SkillsAgentService skillsAgentService;

    private final UserProfileExtractorService userProfileExtractorService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${skillhub.profilepicture.path}")
    private String profilePicturePath;

    @Value("${skillhub.frontend}")
    private String server;

    public ConfirmedUserResponse confirmUser(String confirmationToken, @Valid UserConfirmRequest userConfirmRequest) {
        var user = userService.findUserByConfirmationToken(confirmationToken, UserToConfirm.class);

        if (user.isEmpty()) {
            throw new InvalidConfirmationToken();
        }

        var userToConfirm = user.get();

        userToConfirm.setPassword(passwordEncoder.encode(userConfirmRequest.getPassword()));
        userToConfirm.setConfirmed(true);

        return createConfirmedUserResponse(userService.save(userToConfirm));
    }

    private ConfirmedUserResponse createConfirmedUserResponse(UserToConfirm user) {
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

        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();

        if (company.getEmployees().stream().noneMatch(user -> user.getId().equals(currentUser.getId()))) {
            throw new InsufficientRights();
        }

        User userToSave = createUserFromRequest(createUserRequest);

        userToSave.getCompanies().add(company);

        User savedUser = userService.save(userToSave);

        emailService.sendEmail(createNewUserEmailRequest(savedUser));

        return createUserResponse(savedUser, company);
    }

    public List<EmployeeDto> getEmployeesAccessibleToCurrentUser(Optional<String> searchString) {
        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        if (searchString.isPresent()) {
            var keywords = skillsAgentService.getKeywordsFromSearchString(searchString.get());
            return getEmployeesAccessibleToUserBySearchString(currentUser.getId(), keywords);
        }
        return getEmployeesAccessibleToUser(currentUser.getId());
    }

    public ProfileDto getProfileForCurrentUser() {
        var currentBaseUser = loadCurrentUser.loadSkillhubUserFromContext();
        var employeeProfile = userService.findEmployeeProfileById(currentBaseUser.getId());
        return getProfileForUser(employeeProfile);
    }

    public ProfileDto getProfileForEmployee(String employeeId) {
        var employeeProfile = userService.findEmployeeProfileById(employeeId);
        return getProfileForUser(employeeProfile);
    }

    public SkillDto addSkillToUser(String userId, String skillId) {
        var skill = skillService.findById(skillId);
        var user = userService.findById(userId, UserWithSkills.class);
        user.getSkills().add(skill);
        userService.save(user);
        return new SkillDto(skill.getId(), skill.getLabel());
    }

    public void removeSkillFromUser(String userId, String skillId) {
        var skill = skillService.findById(skillId);
        var user = userService.findById(userId, UserWithSkills.class);
        user.getSkills().remove(skill);
        userService.save(user);
    }

    public String saveUserProfilePicture(String userId, MultipartFile profilePicture) { // should we only accept specific filetypes???
        File directory = new File(profilePicturePath);
        String extension = ".jpg";
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (profilePicture.getOriginalFilename() != null) {
            String originalFilename = profilePicture.getOriginalFilename();
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
        }
        String filename = userId + extension;
        Path filePath = Paths.get(profilePicturePath, filename);
        
        // Copy the file contents from the resource to the target file
        // Existing file gets overwritten
        try (InputStream in = profilePicture.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            var user = userService.findById(userId,UserToConfirm .class);
            user.setProfilePictureLocation(filename);
            userService.save(user);
        } catch (IOException e) {
            throw new ProfilePictureNotSavedException(); //should we include the message?
        }
        return "Profile picture saved successfully";
    }

    private ProfileDto getProfileForUser(EmployeeProfile user) {
        var profile = new ProfileDto();
        profile.setFullname(user.getFullName());
        profile.setEmail(user.getEmail());
        profile.setId(user.getId());
        profile.setCertificates(user.getHasCertificates().stream().map(this::convertToCertificateDto).toList());
        profile.setExperiences(user.getExperiences().stream().map(this::convertToExperienceDto).toList());
        profile.setSkills(user.getSkills().stream().map(this::convertToSkillDto).toList());
        profile.setLanguages(user.getSpeaks().stream().map(this::convertSpeaksToLanguageDto).toList());
        profile.setResumes(user.getResumes().stream().map(this::convertToResumeDto).toList());
        return profile;
    }

    private ProfileDtoResumesInner convertToResumeDto(EmployeeProfile.Resume resume) {
        var newResume = new ProfileDtoResumesInner();
        newResume.setId(resume.getId());
        newResume.setTitle(resume.getTitle());
        // newResume.set(convertToOccupationalCategoryDto(resume.getRole()));
        return newResume;
    }

    private LanguageDto convertSpeaksToLanguageDto(EmployeeProfile.Speaks speaks) {
        var language = speaks.getLanguage();
        return new LanguageDto(language.getLanguageName(), language.getLanguageCode().getName());
    }

    private SkillDto convertToSkillDto(EmployeeProfile.Skill skill) {
        var skillDto = new SkillDto(skill.getId(), skill.getLabel());
        return skillDto;
    }

    private CertificateDto convertToCertificateDto(EmployeeProfile.HasCertificate hasCertificate) {
        var certificateDto = new CertificateDto();
        certificateDto.setId(hasCertificate.getId());
        certificateDto.setIssuer(hasCertificate.getCertificate().getIssuer());
        certificateDto.setName(hasCertificate.getCertificate().getName());
        certificateDto.setIssuedDate(hasCertificate.getIssuedDate());
        certificateDto.setExpirationDate(Optional.ofNullable(hasCertificate.getExpirationDate()));
        certificateDto.setFile(Optional.ofNullable(hasCertificate.getFile()));
        return certificateDto;
    }

    private ExperienceDto convertToExperienceDto(EmployeeProfile.Experience experience) {
        var experienceDto = new ExperienceDto();
        experienceDto.setId(Optional.of(experience.getId()));
        experienceDto.setTitle(experience.getTitle());
        experienceDto.setResponsibilities(experience.getDescriptions());
        experienceDto.setSkills(experience.getSkills().stream().map(this::convertToSkillDto).toList());
        experienceDto.setStartDate(Optional.ofNullable(experience.getStartDate()));
        experienceDto.setEndDate(Optional.ofNullable(experience.getEndDate()));
        // experienceDto.setIndustry(Optional.ofNullable(experience.getIndustries().stream().map(this::convertToIndustryDto).findFirst().orElse(null)));
        experienceDto.setOccupationalCategory(convertToOccupationalCategoryDto(experience.getOccupation()));
        return experienceDto;
    }

    private OccupationalCategoryDto convertToOccupationalCategoryDto(EmployeeProfile.Occupation occupation) {
        if (occupation == null) {
            return null;
        }
        return new OccupationalCategoryDto(occupation.getId(), occupation.getLabel());
    }

    public List<EmployeeDto> getEmployeeAccessibleToUserWithId(String userId) {
        return getEmployeesAccessibleToUser(userId);
    }

    public ProfileDto extractInformationFromCvPdf(String userId, MultipartFile cvPdf) {
        userProfileExtractorService.extractInformationFromCvPdf(userId, cvPdf);
        var employeeProfile = userService.findEmployeeProfileById(userId);
        return getProfileForUser(employeeProfile);
    }

    private List<EmployeeDto> getEmployeesAccessibleToUser(String userId) {
        var companies = companyService.findMinimalCompanyByEmployeesId(userId);
        List<String> companyIds = companies.stream().map(MinimalCompany::getId).toList();
        return userService.findByCompaniesIdIn(companyIds).stream().map(this::convertUserToEmployeeDto).toList();
    }

    private List<EmployeeDto> getEmployeesAccessibleToUserBySearchString(String userId, List<String> keywords) {
        var companies = companyService.findMinimalCompanyByEmployeesId(userId);
        List<String> companyIds = companies.stream().map(MinimalCompany::getId).toList();
        return userService.findByCompaniesAndKeyWords(companyIds, keywords).stream().map(this::convertUserToEmployeeDto)
                .toList();
    }

    private EmployeeDto convertUserToEmployeeDto(Employee user) {
        return new EmployeeDto()
                .id(user.getId())
                .fullname(user.getFullName())
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

    private EmailRequest createNewUserEmailRequest(User user) {
        EmailRequest email = new EmailRequest();
        email.setRecipient(user.getEmail());
        // email.setMessage(createNewUserMessage());
        email.setTemplate("new-user");
        Context context = new Context();
        var stringBuilder = new StringBuilder();
        stringBuilder.append(server);
        stringBuilder.append("/confirm/");
        stringBuilder.append(user.getConfirmationToken());
        context.setVariable("link", stringBuilder.toString());
        context.setVariable("username", user.getFullname());
        email.setSubject("An account has been created for you");
        email.setContext(context);
        return email;
    }

}
