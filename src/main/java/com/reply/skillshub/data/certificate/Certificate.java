package com.reply.skillshub.data.certificate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Node
@Data
public class Certificate {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @NotEmpty
    private String name;

    private String issuer = "";
}

