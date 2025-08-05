package com.reply.skillshub.data.client;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ClientRepository extends Neo4jRepository<Client, String> {

    List<Client> findByNameIgnoreCase(String name);
  
}
