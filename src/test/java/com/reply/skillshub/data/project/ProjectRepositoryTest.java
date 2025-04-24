package com.reply.skillshub.data.project;

import static org.instancio.Select.field;

import java.util.List;

import org.instancio.Instancio;
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
    Project project = Instancio.of(Project.class).set(field(Project::getIndustry), null)
        .set(field(Project::getUsers), List.of()).set(field(Project::getClient), null).create();
    project.setId(null);
    Project savedProject = projectRepository.save(project);
    Assertions.assertNotNull(savedProject.getId());
  }

}
