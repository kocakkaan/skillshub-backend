package com.reply.skillshub.data.client;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.contact.Contact;

import lombok.Data;

@Node
@Data
public class Client {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)    
  private String id;

  private String name;

  @Relationship(type = "WORKS_FOR", direction = Relationship.Direction.INCOMING)
  private List<Contact> contacts;
}
