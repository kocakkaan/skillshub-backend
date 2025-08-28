package com.reply.skillshub.controllers.users;

import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reply.skillshub.openapi.api.UsersApi;
import com.reply.skillshub.openapi.model.ConfirmedUserResponse;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;
import com.reply.skillshub.openapi.model.EmployeeDto;
import com.reply.skillshub.openapi.model.LanguageSkillDto;
import com.reply.skillshub.openapi.model.ProfileDto;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.UpdateProfileDto;
import com.reply.skillshub.openapi.model.UserConfirmRequest;
import com.reply.skillshub.openapi.model.UserMeProfilePicturePostRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UsersControllerService usersControllerService;

    @Override
    public ResponseEntity<CreatedUserResponse> companyCompanyIdEmployeesPost(String companyId,
            CreateUserRequest createUserRequest) {
        return ResponseEntity.status(201)
                .body(usersControllerService.addNewUserToCompany(companyId, createUserRequest));
    }

    @Override
    public ResponseEntity<ConfirmedUserResponse> usersConfirmationConfirmationTokenPut(String confirmationToken,
            @Valid UserConfirmRequest userConfirmRequest) {
        return ResponseEntity.status(201)
                .body(usersControllerService.confirmUser(confirmationToken, userConfirmRequest));
    }

    @Override
    public ResponseEntity<Void> userMeProfilePicturePost(
            @Valid UserMeProfilePicturePostRequest userMeProfilePicturePostRequest) {
        String base64URL = userMeProfilePicturePostRequest.getBase64URL()
                .orElseThrow(() -> new IllegalArgumentException("base64URL is required"));
        usersControllerService.saveUserProfilePictureFromBase64(base64URL);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> userMeEmployeesGet(Optional<String> searchString) {
        return ResponseEntity.ok(usersControllerService.getEmployeesAccessibleToCurrentUser(searchString));
    }

    @Override
    public ResponseEntity<ProfileDto> userMeProfileGet() {
        return ResponseEntity.ok(usersControllerService.getProfileForCurrentUser());
    }

    @Override
    public ResponseEntity<ProfileDto> usersUserIdProfileGet(String userId) {
        return ResponseEntity.ok(usersControllerService.getProfileForEmployee(userId));
    }

    @Override
    public ResponseEntity<String> usersUserIdProfilePicturePost(String userId, MultipartFile profilePicture) {
        return ResponseEntity.ok(usersControllerService.saveUserProfilePicture(userId, profilePicture));
    }

    @Override
    public ResponseEntity<SkillDto> usersUserIdSkillsSkillIdPost(String userId, String skillId) {
        return ResponseEntity.ok(usersControllerService.addSkillToUser(userId, skillId));
    }

    @Override
    public ResponseEntity<ProfileDto> usersUserIdProfilePost(String userId, MultipartFile body) {
        return ResponseEntity.ok(usersControllerService.extractInformationFromCvPdf(userId, body));
    }

    @Override
    public ResponseEntity<Void> usersUserIdSkillsSkillIdDelete(String userId, String skillId) {
        usersControllerService.removeSkillFromUser(userId, skillId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Resource> usersUserIdProfilePictureGet(String userId) {
        try {
            Resource profilePicture = usersControllerService.getUserProfilePicture(userId);
            String contentType = usersControllerService.getProfilePictureContentType(userId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(profilePicture);
        } catch (com.reply.skillshub.base.exceptionhandling.exeptions.ProfilePictureNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Resource> userMeProfilePictureGet() {
        try {
            Resource profilePicture = usersControllerService.getProfilePictureForCurrentUser();
            String contentType = usersControllerService.getProfilePictureContentTypeForCurrentUser();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(profilePicture);
        } catch (com.reply.skillshub.base.exceptionhandling.exeptions.ProfilePictureNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ProfileDto> usersUserIdProfilePut(String userId, UpdateProfileDto updateProfileDto) {
        return ResponseEntity.ok(usersControllerService.updateUserProfileDto(userId, updateProfileDto));
    }

    @Override
    public ResponseEntity<List<LanguageSkillDto>> usersUserIdLanguagesPut(String userId,
            List<LanguageSkillDto> languageSkillDto) {
        return ResponseEntity.ok(usersControllerService.addLanguagesToUser(userId, languageSkillDto));
    }

}
