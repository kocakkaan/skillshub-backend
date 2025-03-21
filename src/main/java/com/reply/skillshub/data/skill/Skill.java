package com.reply.skillshub.data.skill;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Node
@Data
public class Skill {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @NotEmpty
    private String label;

}
