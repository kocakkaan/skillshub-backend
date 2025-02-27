package com.reply.skillshub.data.resume;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeNotFound;
import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final Neo4jTemplate neo4jTemplate;

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

    public <T> T findById(String id, Class<T> type) {
        return repository.findById(id, type).orElseThrow(() -> new ResumeNotFound());
    }

    public Resume save(Resume resume) {
        validationHandler.validate(resume);
        return repository.save(resume);
    }

    // T needs to have something in common with User.class
    public <T> T save(T resume) {
        return neo4jTemplate.save(Resume.class).one(resume);
    }
    
}
