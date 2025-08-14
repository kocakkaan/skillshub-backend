package com.reply.skillshub.data.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class ProjectRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private ProjectRepository projectRepository;

  @Test
  void succesfullySaveProject() {
    Project newProject = new Project();
    newProject.setTitle("Test Project");
    newProject.setDescription("This is a test project description.");
    newProject.setProjectId(1);
    Project savedProject = projectRepository.save(newProject);
    Assertions.assertNotNull(savedProject.getId());
  }

}
