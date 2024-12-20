package com.reply.skillshub.data.resume;

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
import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.User;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Node
public class Resume {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    private String background;

    @NotEmpty
    private String title;

    // This should be occupation probably
    @Relationship(type = "FOR_ROLE", direction = Direction.OUTGOING)
    private List<Occupation> role = new ArrayList<>();

    @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING, cascadeUpdates = false)
    private List<Industry> industries = new ArrayList<>();

    @Relationship(type = "USED_SKILL", direction = Direction.OUTGOING)
    private List<ResumeSkill> skills = new ArrayList<>();

    @Relationship(type = "GAINED_EXPERIENCE", direction = Direction.OUTGOING)
    private List<ResumeExperience> experiences = new ArrayList<>();

    @Relationship(type = "HAS_RESUME", direction = Direction.INCOMING)
    private List<User> users = new ArrayList<>();

    @AssertTrue(message = "A resume can have at most five industries assigned")
    private boolean isIndustryCountLowerAsFive() {
        return industries.size() < 5;
    }

    @AssertTrue(message = "A resume can have at most one role")
    private boolean isRoleCountLowerAsTwo() {
        return role.size() < 2;
    }

    @AssertTrue(message = "A resume must have at least one user")
    private boolean isUserSizeExactlyOne() {
        return users.size() == 1;
    }

    public User getUser() {
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public Resume setUser(User user) {
        users.clear();
        users.add(user);
        return this;
    }

    public Optional<Occupation> getOptionalRole() {
        if (role.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(role.get(0));
    }

}
