package com.reply.skillshub.data.resumeexperience;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.resume.Resume;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
@Node
public class ResumeExperience {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    private List<String> descriptions = new ArrayList<>();

    @Relationship(type = "BASED_OF", direction = Direction.OUTGOING)
    private List<Experience> basedOfExperience = new ArrayList<>();

    @Relationship(type = "GAINED_EXPERIENCE", direction = Direction.INCOMING)
    private List<Resume> resumes = new ArrayList<>();

    @AssertTrue
    private boolean isBasedOfExperienceEqualToOne() {
        return basedOfExperience.size() == 1;
    }

    @AssertFalse(message = "An experience should not have more as four descriptions")
    private boolean isDescriptionSizeSmallerAsFive() {
        return descriptions.size() > 4;
    }

    public Optional<Experience> getBasedOf() {
        if (basedOfExperience.size() > 0) {
            return Optional.of(basedOfExperience.get(0));
        }
        return Optional.empty();
    }
    
}
