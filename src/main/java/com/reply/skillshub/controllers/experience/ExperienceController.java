package com.reply.skillshub.controllers.experience;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ExperiencesApi;
import com.reply.skillshub.openapi.model.BaseExperience;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExperienceController implements ExperiencesApi {

    private final ExperienceControllerService experienceControllerService;

    @Override
    public ResponseEntity<Void> experiencesExperienceIdDelete(String experienceId) {
        experienceControllerService.deleteExperienceById(experienceId);
        return ResponseEntity.status(204).build();
    }

    @Override
    public ResponseEntity<BaseExperience> experiencesExperienceIdGet(String experienceId) {
        return ResponseEntity.ok(experienceControllerService.getExperienceById(experienceId));
    }

    @Override
    public ResponseEntity<BaseExperience> experiencesExperienceIdPut(String experienceId, @Valid BaseExperience baseExperience) {
        return ResponseEntity.ok(experienceControllerService.updateExperienceForUser(experienceId, baseExperience));
    }

    @Override
    public ResponseEntity<List<BaseExperience>> usersUserIdExperiencesGet(String userId) {
        return ResponseEntity.ok(experienceControllerService.getExperiencesByUserId(userId));
    }

    @Override
    public ResponseEntity<BaseExperience> usersUserIdExperiencesPost(String userId, @Valid BaseExperience baseExperience) {
        return ResponseEntity.ok(experienceControllerService.createExperienceForUser(userId, baseExperience));
    }
    
}
