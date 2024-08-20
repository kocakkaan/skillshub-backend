package com.reply.skillshub.data.experience;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.reply.skillshub.base.services.ValidationHandler;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    private final ValidationHandler<Experience> validationHandler; 

    public void deleteExperienceById(String id) {
        experienceRepository.deleteById(id);
    }

    public Experience save(Experience experience) {
        validationHandler.validate(experience);
        return experienceRepository.save(experience);

    }
    
}
