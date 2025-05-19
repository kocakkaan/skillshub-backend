package com.reply.skillshub.data.projectcounter;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class ProjectCounter {

  @Id
  private String id = "projectCounter";	
  private int projectCount;

  public int getNextProjectId() {
    return projectCount + 1;
  }
  
} 