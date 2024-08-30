package com.reply.skillshub.data.resume;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository repository;

    private final ValidationHandler<Resume> validationHandler;

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Optional<Resume> findById(String id) {
        return repository.findById(id);
    }

    public Resume save(Resume resume) {
        validationHandler.validate(resume);
        return repository.save(resume);
    }
    
}
