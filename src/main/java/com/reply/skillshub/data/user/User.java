package com.reply.skillshub.data.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.BaseNode;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.userrole.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Node
@Data
public class User {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)    
    private String id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    
    @Email
    @NotEmpty
    private String email;

    @NotNull
    private UserRole userRole;

    @NotEmpty
    private String password; 

    @Relationship(type = "SPEAKS")
    private List<Speaks> speaks = new ArrayList<>();
    
}
