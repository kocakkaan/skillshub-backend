package com.reply.skillshub.data.skill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.user.User;

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
