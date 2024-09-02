package com.reply.skillshub.data.resume;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Node
public class Resume {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    @NotEmpty
    private String background;

    @NotEmpty
    private String title;

    // This should be occupation probably
    private String role;

    @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING)
    private List<Industry> industries = new ArrayList<>();

    @Relationship(type = "USED_SKILL", direction = Direction.OUTGOING)
    private List<ResumeSkill> skills = new ArrayList<>();

    @Relationship(type = "GAINED_EXPERIENCE", direction = Direction.OUTGOING)
    private List<ResumeExperience> experiences = new ArrayList<>();

    @AssertTrue
    private boolean isIndustryCountLowerAsTwo() {
        return industries.size() < 2;
    }

}
