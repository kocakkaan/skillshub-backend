package com.reply.skillshub.data.contact;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.client.Client;
import com.reply.skillshub.data.project.Project;

import lombok.Data;

@Data
@Node
public class Contact {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;
  private String name;
  private String email;
  private String phoneNumber;

  @Relationship(type = "WORKS_FOR", direction = Relationship.Direction.OUTGOING)
  private Client company;

  @Relationship(type = "CONTACT_FOR", direction = Relationship.Direction.INCOMING)
  private List<Project> projects;

}
