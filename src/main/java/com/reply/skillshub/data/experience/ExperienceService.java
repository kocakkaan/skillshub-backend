package com.reply.skillshub.data.experience;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.reply.skillshub.base.exceptionhandling.exeptions.ExperienceNotFound;
import com.reply.skillshub.base.services.ValidationHandler;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    private final ValidationHandler<Experience> validationHandler; 

    public void deleteExperienceById(String id) {
        experienceRepository.deleteById(id);
    }

    public Optional<Experience> findById(String id) {
        return experienceRepository.findById(id);
    }

    public Experience loadById(String id) {
        return experienceRepository.findById(id).orElseThrow(() -> new ExperienceNotFound());
    }

    public List<Experience> findByUserId(String userId) {
        return experienceRepository.findAllByEmployeesId(userId);
    }

    public Experience save(Experience experience) {
        validationHandler.validate(experience);
        return experienceRepository.save(experience);
    }
    
}
