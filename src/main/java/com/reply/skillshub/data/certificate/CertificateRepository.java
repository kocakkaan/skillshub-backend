package com.reply.skillshub.data.certificate;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface CertificateRepository extends Neo4jRepository<Certificate, String> {

    public Optional<Certificate> findByName(String name);

}
