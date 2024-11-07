package com.reply.skillshub.data.resume;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeNotFound;
import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository repository;

    private final ValidationHandler<Resume> validationHandler = new ValidationHandler<>();

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Optional<Resume> findOptionalById(String id) {
        return repository.findById(id);
    }

    public Resume findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResumeNotFound());
    }

    public List<Resume> findByUserId(String userId) {
        return repository.findAllByUsersId(userId);
    }

    public Resume save(Resume resume) {
        validationHandler.validate(resume);
        return repository.save(resume);
    }
    
}
