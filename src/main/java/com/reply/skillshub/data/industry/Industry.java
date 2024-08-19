package com.reply.skillshub.data.industry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.experience.Experience;

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
    
    @Relationship(type = "IN_INDUSTRY", direction = Direction.INCOMING)
    private List<Experience> experiences = new ArrayList<>();
    
}
