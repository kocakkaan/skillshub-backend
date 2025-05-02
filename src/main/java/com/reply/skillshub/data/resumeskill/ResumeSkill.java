package com.reply.skillshub.data.resumeskill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.Data;

@Data
@Node
public class ResumeSkill {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private int index;

    private String parent;

    private List<String> skills = new ArrayList<>();

}
