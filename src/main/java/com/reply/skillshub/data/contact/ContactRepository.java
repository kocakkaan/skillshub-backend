package com.reply.skillshub.data.contact;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ContactRepository extends Neo4jRepository<Contact, String> {
  
}
