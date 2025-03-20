package com.reply.skillshub.data.experience;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.skill.Skill;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Node
@Data
public class Experience {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;
    
    @NotEmpty
    private String title;

    private List<String> descriptions = new ArrayList<>();

    private LocalDate startDate;

    private LocalDate endDate;

    @Relationship(type = "INVOLVES_SKILL", direction = Direction.OUTGOING, cascadeUpdates = false)
    private List<Skill> skills = new ArrayList<>();

    @Relationship(type = "WORKED_IN", direction = Direction.OUTGOING, cascadeUpdates = false)
    private List<Language> languages = new ArrayList<>();

    @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING, cascadeUpdates = false)
    private List<Industry> industries = new ArrayList<>();

    @NotNull
    @Relationship(type = "IN_ROLE", direction = Direction.OUTGOING, cascadeUpdates = false)
    private Occupation occupation;

    @AssertTrue(message = "An experience can have at most one industry")
    private boolean isIndustrySmallerAsTwo() {
        return industries.size() < 2;
    }

    @AssertFalse(message = "An experience should not have more as six descriptions")
    private boolean isDescriptionSizeSmallerAsSix() {
        return descriptions.size() > 6;
    }

    public Optional<Industry> getIndustry() {
        if (industries.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(industries.get(0));
    }

}
