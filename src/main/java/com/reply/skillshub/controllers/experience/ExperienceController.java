package com.reply.skillshub.controllers.experience;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ExperiencesApi;
import com.reply.skillshub.openapi.model.ExperienceDto;

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
    public ResponseEntity<ExperienceDto> experiencesExperienceIdGet(String experienceId) {
        return ResponseEntity.ok(experienceControllerService.getExperienceById(experienceId));
    }

    @Override
    public ResponseEntity<ExperienceDto> experiencesExperienceIdPut(String experienceId, ExperienceDto ExperienceDto) {
        return ResponseEntity.ok(experienceControllerService.updateExperienceById(experienceId, ExperienceDto));
    }

    @Override
    public ResponseEntity<List<ExperienceDto>> usersUserIdExperiencesGet(String userId) {
        return ResponseEntity.ok(experienceControllerService.getExperiencesByUserId(userId));
    }

    @Override
    public ResponseEntity<ExperienceDto> usersUserIdExperiencesPost(String userId, @Valid ExperienceDto ExperienceDto) {
        return ResponseEntity.ok(experienceControllerService.createExperienceForUser(userId, ExperienceDto));
    }
    
}
