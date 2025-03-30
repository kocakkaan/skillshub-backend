package com.reply.skillshub.data.industry;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Node
@Data
public class Industry {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @NotEmpty
    private String label;

}
