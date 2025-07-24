package com.reply.skillshub.data.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeNotFound;
import com.reply.skillshub.base.services.ValidationHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final Neo4jTemplate neo4jTemplate;

    private final ProjectRepository repository;

    private final ValidationHandler<Project> validationHandler = new ValidationHandler<>();

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<ProjectRead> findAll() {
        return repository.findAllBy(ProjectRead.class);
    }

    public Optional<Project> findOptionalById(String id) {
        return repository.findById(id);
    }

    public Project findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResumeNotFound());
    }

    public ProjectRead findReferenceById(String id) {
        return repository.findById(id, ProjectRead.class)
                .orElseThrow(() -> new ResumeNotFound());
    }

    public ProjectReadWithReference findReferenceWithReferencesById(String id) {
        return repository.findById(id, ProjectReadWithReference.class)
                .orElseThrow(() -> new ResumeNotFound());
    }

    public <T> T findById(String id, Class<T> type) {
        return repository.findById(id, type).orElseThrow(() -> new ResumeNotFound());
    }

    public Project save(Project resume) {
        validationHandler.validate(resume);
        return repository.save(resume);
    }

    // T needs to have something in common with User.class
    public <T> T save(T resume) {
        return neo4jTemplate.save(Project.class).one(resume);
    }

    public Optional<Project> findByProjectReferenceId(String projectReferenceId) {
        return repository.findByProjectReferenceId(projectReferenceId);
    }

}
