package com.reply.skillshub.controllers.users;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.reply.skillshub.base.exceptionhandling.exeptions.InsufficientRights;
import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidConfirmationToken;
import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidFileTypeException;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProfilePictureNotFoundException;
import com.reply.skillshub.base.exceptionhandling.exeptions.ProfilePictureNotSavedException;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
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
import com.reply.skillshub.openapi.model.EmployeeDtoResumesInner;
import com.reply.skillshub.openapi.model.ExperienceDto;
import com.reply.skillshub.openapi.model.LanguageDto;
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.ProfileDto;
import com.reply.skillshub.openapi.model.ProfileDtoResumesInner;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.UserConfirmRequest;
import com.reply.skillshub.services.LinkGeneratorService;
import com.reply.skillshub.services.SkillsAgentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersControllerService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UsersControllerService.class);

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


    private final LinkGeneratorService linkGeneratorService;

    private boolean isAdmin() {
        return loadCurrentUser.loadSkillhubUserFromContext().getUserRole() == UserRole.ADMIN;
    }

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
        response.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getFullname() : null);
        response.setCreatedOn(user.getCreatedOn());
        return response;
    }

    public CreatedUserResponse addNewUserToCompany(@NotEmpty String companyId, CreateUserRequest createUserRequest) {
        Company company = companyService
                .findById(companyId)
                .orElseThrow(() -> new NoCompanyFound());

        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();

        logger.info("Current user ID: {} started process to create user {}", currentUser.getId(), createUserRequest.getEmail());

        if (company.getEmployees().stream().noneMatch(user -> user.getId().equals(currentUser.getId()))) {
            throw new InsufficientRights();
        }

        User userToSave = createUserFromRequest(createUserRequest, currentUser.getId());

        userToSave.getCompanies().add(company);

        User savedUser = userService.save(userToSave);

        emailService.sendEmail(createNewUserEmailRequest(savedUser));
        logger.info("User {} created successfully with ID: {}", createUserRequest.getEmail(), savedUser.getId());
        return createUserResponse(savedUser, company);
    }

    public List<EmployeeDto> getEmployeesAccessibleToCurrentUser(Optional<String> searchString) {
        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        if (searchString.isPresent() && !searchString.get().isBlank()) {
            return getEmployeesAccessibleToUserBySearchString(currentUser.getId(), searchString.get());
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

    public String saveUserProfilePicture(String userId, MultipartFile profilePicture) {
        if (userId == null) {
            BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
            userId = currentUser.getId();
        }

        File directory = new File(profilePicturePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = profilePicture.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new InvalidFileTypeException("File must have a valid extension.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Set<String> allowedExtensions = Set.of(".jpg", ".jpeg", ".png");
        if (!allowedExtensions.contains(extension)) {
            throw new InvalidFileTypeException("Only JPG, JPEG and PNG formats are supported.");
        }

        String filename = userId + extension;
        Path filePath = Paths.get(profilePicturePath, filename);

        try (InputStream in = profilePicture.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            var user = userService.findById(userId, UserToConfirm.class);
            user.setProfilePictureLocation(filename);
            userService.save(user);
        } catch (IOException e) {
            throw new ProfilePictureNotSavedException();
        }

        return "Profile picture saved successfully";
    }

    public Resource getUserProfilePicture(String userId) {
        var user = userService.findById(userId, UserToConfirm.class);
        var profilePictureLocation = user.getProfilePictureLocation();
        if (profilePictureLocation == null) {
            throw new ProfilePictureNotFoundException();
        }

        Path filePath = Paths.get(profilePicturePath, profilePictureLocation);
        if (!Files.exists(filePath)) {
            throw new ProfilePictureNotFoundException();
        }

        return new FileSystemResource(filePath.toString());
    }

    public String getProfilePictureContentType(String userId) {
        var user = userService.findById(userId, UserToConfirm.class);
        var profilePictureLocation = user.getProfilePictureLocation();
        if (profilePictureLocation == null) {
            throw new ProfilePictureNotFoundException();
        }

        String extension = profilePictureLocation.toLowerCase();
        if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        return "image/jpeg";
    }

    public void saveUserProfilePictureFromBase64(String base64URL) {
        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        String userId = currentUser.getId();

        String base64Image = base64URL;
        if (base64URL.contains(",")) {
            base64Image = base64URL.split(",")[1];
        }

        String extension = ".png";
        if (base64URL.contains("data:image/jpeg")) {
            extension = ".jpg";
        } else if (base64URL.contains("data:image/png")) {
            extension = ".png";
        }

        File directory = new File(profilePicturePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = userId + extension;
        Path filePath = Paths.get(profilePicturePath, filename);

        try {
            byte[] decodedImg = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
            Files.write(filePath, decodedImg);

            var user = userService.findById(userId, UserToConfirm.class);
            user.setProfilePictureLocation(filename);
            userService.save(user);
        } catch (IOException e) {
            throw new ProfilePictureNotSavedException("Failed to save profile picture: " + e.getMessage());
        }
    }

    public Resource getProfilePictureForCurrentUser() {
        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        return getUserProfilePicture(currentUser.getId());
    }

    public String getProfilePictureContentTypeForCurrentUser() {
        BaseUser currentUser = loadCurrentUser.loadSkillhubUserFromContext();
        return getProfilePictureContentType(currentUser.getId());
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

    private List<EmployeeDto> getEmployeesAccessibleToUserBySearchString(String userId, String searchString) {
        var companies = companyService.findMinimalCompanyByEmployeesId(userId);
        List<String> companyIds = companies.stream().map(MinimalCompany::getId).toList();
        
        // Get matching employee IDs directly from agent service
        List<String> matchingEmployeeIds = skillsAgentService.getMatchingEmployeeIds(searchString, companyIds);
        
        // Fetch employees by IDs
        List<Employee> employees = userService.findEmployeesByIds(matchingEmployeeIds);
        
        return employees.stream()
                .map(this::convertUserToEmployeeDto)
                .toList();
    }

    private EmployeeDto convertUserToEmployeeDto(Employee user) {
        EmployeeDto dto = new EmployeeDto()
                .id(user.getId())
                .fullname(user.getFullName())
                .role(user.getUserRole().name())
                .confirmed(user.getConfirmed())
                .confirmationLink(linkGeneratorService.getConfirmationLink(user))
                .resumes(user.getResumes().stream().map(this::convertToDtoResume).toList())
                .createdBy(isAdmin() && user.getCreatedBy() != null ? user.getCreatedBy().getFullName() : null)
                .createdOn(isAdmin() ? user.getCreatedOn() : null);
        return dto;
    }

    private EmployeeDtoResumesInner convertToDtoResume(Employee.Resume resume) {
        var resumeDto =  new EmployeeDtoResumesInner();
        resumeDto.setId(resume.getId());
        resumeDto.setRole(resume.getRole());
        return resumeDto;
    }

    private User createUserFromRequest(CreateUserRequest createUserRequest, String creatorId) {
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setUserRole(UserRole.EMPLOYEE);
        user.setPassword("tempPassword");

        User creator = userService.findById(creatorId, User.class);
        if (creator == null) {
            throw new UserNotFound("Creator user not found with ID: " + creatorId);
        }
        user.setCreatedBy(creator);
        user.setCreatedUserId(creatorId);
        user.setCreatedOn(java.time.LocalDate.now());
        return user;
    }

    private CreatedUserResponse createUserResponse(User user, Company company) {
        CreatedUserResponse response = new CreatedUserResponse();
        response.companyId(company.getId());
        response.email(user.getEmail());
        response.fullName(user.getFullname());
        response.id(user.getId());
        response.role(user.getUserRole().name());
        response.setCreatedBy(isAdmin() && user.getCreatedBy() != null ? user.getCreatedBy().getFullname() : null);
        response.setCreatedOn(isAdmin() ? user.getCreatedOn() : null);
        return response;
    }

    private EmailRequest createNewUserEmailRequest(User user) {
        EmailRequest email = new EmailRequest();
        email.setRecipient(user.getEmail());
        // email.setMessage(createNewUserMessage());
        email.setTemplate("new-user");
        Context context = new Context();
        context.setVariable("link", linkGeneratorService.getConfirmationLink(user));
        context.setVariable("username", user.getFullname());
        email.setSubject("An account has been created for you");
        email.setContext(context);
        return email;
    }

}
