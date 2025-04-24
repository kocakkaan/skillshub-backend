package com.reply.skillshub.data.client;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.Data;

@Node
@Data
public class Client {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)    
  private String id;

  private String name;
}
