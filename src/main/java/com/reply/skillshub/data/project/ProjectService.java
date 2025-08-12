package com.reply.skillshub.data.project;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.ResumeNotFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.ValidationHandler;
import com.reply.skillshub.data.client.Client;
import com.reply.skillshub.data.contact.Contact;

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
        var contact = resume.getContact();
        if (contact != null) {
            var clients = resume.getClients();
            if (clients == null || clients.isEmpty()) {
                throw new ValidationException("When a contact is present, a resume must have a contact");
            }

            if (!contactExistsInClients(clients, contact)) {
                throw new ValidationException("Contact must be part of the clients in the project");
            }
        }
        validationHandler.validate(resume);
        return repository.save(resume);
    }

    private static boolean contactExistsInClients(List<Client> clients, Contact contact) {
        if (contact == null || contact.getId() == null) {
            return false;
        }

        return clients.stream()
                .filter(Objects::nonNull)
                .map(Client::getContacts)
                .filter(Objects::nonNull)
                .anyMatch(contacts -> contacts.stream()
                        .anyMatch(contactClient -> contact.getId().equals(contactClient.getId())));
    }

    // T needs to have something in common with User.class
    public <T> T save(T resume) {
        return neo4jTemplate.save(Project.class).one(resume);
    }

}
