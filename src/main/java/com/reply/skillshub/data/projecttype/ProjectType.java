package com.reply.skillshub.data.projecttype;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class ProjectType {
  
  @Id
  private String name;
}
