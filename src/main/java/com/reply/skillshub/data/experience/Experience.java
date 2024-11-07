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
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.User;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
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

    @Relationship(type = "HAS_EXPERIENCE", direction = Direction.INCOMING)
    private List<User> employees = new ArrayList<>();

    @Relationship(type = "GAINED_IN", direction = Direction.INCOMING)
    private List<Skill> skills = new ArrayList<>();

    @Relationship(type = "WORKED_IN", direction = Direction.OUTGOING)
    private List<Language> languages = new ArrayList<>();

    @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING)
    private List<Industry> industries = new ArrayList<>();

    @Relationship(type = "HAD_OCCUPATION", direction = Direction.OUTGOING)
    private List<Occupation> occupation = new ArrayList<>();

    @Relationship(type = "BASED_OF", direction = Direction.INCOMING)
    private List<ResumeExperience> dependentResumeExperiences;
    
    @AssertTrue(message = "An experience must be assigned exactly one employee")
    private boolean isCountEmployeesEqualToOne() {
        return employees.size() == 1;
    }

    @AssertTrue(message = "An experience can have at most one industry")
    private boolean isIndustrySmallerAsTwo() {
        return industries.size() < 2;
    }

    @AssertFalse(message = "An experience should not have more as four descriptions")
    private boolean isDescriptionSizeSmallerAsFour() {
        return descriptions.size() > 4;
    }

    public Optional<Industry> getIndustry() {
        if (industries.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(industries.get(0));
    }

    public User getEmployee() {
        return employees.get(0);
    }
}
