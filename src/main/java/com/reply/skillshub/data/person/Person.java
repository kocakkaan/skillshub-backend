package com.reply.skillshub.data.person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.language.Language;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Node
@Data
public class Person {

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

    @Relationship(type = "SPEAKS")
    List<Language> languages = new ArrayList<>();
    
}
