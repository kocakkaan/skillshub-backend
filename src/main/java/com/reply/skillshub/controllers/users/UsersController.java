package com.reply.skillshub.controllers.users;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reply.skillshub.openapi.api.UsersApi;
import com.reply.skillshub.openapi.model.ConfirmedUserResponse;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.CreatedUserResponse;
import com.reply.skillshub.openapi.model.EmployeeDto;
import com.reply.skillshub.openapi.model.ProfileDto;
import com.reply.skillshub.openapi.model.SkillDto;
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
        return ResponseEntity.status(201).body(usersControllerService.addNewUserToCompany(companyId, createUserRequest));
    }

    @Override
    public ResponseEntity<ConfirmedUserResponse> usersConfirmationConfirmationTokenPut(String confirmationToken, @Valid UserConfirmRequest userConfirmRequest) {
        return ResponseEntity.status(201).body(usersControllerService.confirmUser(confirmationToken, userConfirmRequest));
    }

    @Override
    public ResponseEntity<Void> userMeProfilePicturePost(
            @Valid UserMeProfilePicturePostRequest userMeProfilePicturePostRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'userMeProfilePicturePost'");
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

}
