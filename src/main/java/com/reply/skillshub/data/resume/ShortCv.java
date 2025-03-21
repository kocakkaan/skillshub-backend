package com.reply.skillshub.data.resume;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Node
public class ShortCv {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private boolean llmGenerated;

    private boolean humanChecked;

    private String background;

    @NotEmpty
    private String title;

    @NotEmpty
    private String role;

    private List<String> industries = new ArrayList<>();

    @Relationship(type = "USED_SKILL", direction = Direction.OUTGOING)
    private List<ResumeSkill> skills = new ArrayList<>();

    @Relationship(type = "GAINED_EXPERIENCE", direction = Direction.OUTGOING)
    private List<ResumeExperience> experiences = new ArrayList<>();

    @AssertTrue(message = "A resume can have at most five industries assigned")
    private boolean isIndustryCountLowerAsFive() {
        return industries.size() < 5;
    }

}
