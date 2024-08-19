package com.reply.skillshub.data.experience;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.User;

import jakarta.validation.constraints.AssertFalse;
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

    private List<String> descriptions;

    @Relationship(type = "HAS_EXPERIENCE", direction = Direction.INCOMING)
    private List<User> employees = new ArrayList<>();

    @Relationship(type = "GAINED_IN", direction = Direction.INCOMING)
    private List<Skill> skills = new ArrayList<>();

    @Relationship(type = "WORKED_IN", direction = Direction.OUTGOING)
    private List<Language> languages = new ArrayList<>();

    @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING)
    private List<Industry> industries = new ArrayList<>();
    
    @AssertFalse
    private boolean isCountEmployeesHigherAsOne() {
        return employees.size() > 1;
    }
}
