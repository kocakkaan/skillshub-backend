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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'experiencesExperienceIdDelete'");
    }

    @Override
    public ResponseEntity<BaseExperience> experiencesExperienceIdGet(String experienceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'experiencesExperienceIdGet'");
    }

    @Override
    public ResponseEntity<Void> experiencesExperienceIdPut(String experienceId, @Valid BaseExperience baseExperience) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'experiencesExperienceIdPut'");
    }

    @Override
    public ResponseEntity<List<BaseExperience>> usersUserIdExperiencesGet(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'usersUserIdExperiencesGet'");
    }

    @Override
    public ResponseEntity<BaseExperience> usersUserIdExperiencesPost(String userId,
            @Valid BaseExperience baseExperience) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'usersUserIdExperiencesPost'");
    }
    
}
