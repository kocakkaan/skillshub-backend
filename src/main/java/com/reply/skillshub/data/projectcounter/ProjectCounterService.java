package com.reply.skillshub.data.projectcounter;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectCounterService {

    private final ProjectCounterRepository repository;

    public ProjectCounter getProjectCounter() {
        return repository.findById("projectCounter").orElse(new ProjectCounter());
    }

    public int incrementProjectId() {
        var projectCounter = getProjectCounter();
        int nextProjectId = projectCounter.getNextProjectId();
        projectCounter.setProjectCount(nextProjectId);
        save(projectCounter);
        return nextProjectId;
    }

    public ProjectCounter save(ProjectCounter counter) {
        return repository.save(counter);
    }


}
