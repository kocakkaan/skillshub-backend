package com.reply.skillshub.data.resumeskill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.skill.Skill;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
@Node
public class ResumeSkill {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    @Relationship(type = "HAS_PARENT", direction = Direction.OUTGOING)
    private List<Skill> parent = new ArrayList<>();

    @Relationship(type = "INCLUDES_SKILL", direction = Direction.OUTGOING)
    private List<Skill> skills = new ArrayList<>();

    @Relationship(type = "USED_SKILL", direction = Direction.INCOMING)
    private List<Resume> resumes = new ArrayList<>();

    @AssertTrue
    private boolean isParentEqualToOne() {
        return parent.size() == 1;
    }

    public Skill getParent() {
        return parent.get(0);
    }
    
}
