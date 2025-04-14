package com.reply.skillshub.data.project;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Node
public class Project {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotEmpty
  private String title;
  private List<String> initialSituation;
  private List<String> challenges;
  private List<String> approachTechnologies;
  private List<String> technologies;
  private String valueAdded1;
  private String valueAdded2;
  private String valueAdded3;
  
}
