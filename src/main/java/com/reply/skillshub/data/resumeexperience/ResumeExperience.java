package com.reply.skillshub.data.resumeexperience;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import jakarta.validation.constraints.AssertFalse;
import lombok.Data;

@Data
@Node
public class ResumeExperience {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    private String title;

    private String role;

    private List<String> descriptions = new ArrayList<>();

    private List<String> technologies = new ArrayList<>();


    @AssertFalse(message = "An experience should not have more as four descriptions")
    private boolean isDescriptionSizeSmallerAsFive() {
        return descriptions.size() > 4;
    }
    
}
