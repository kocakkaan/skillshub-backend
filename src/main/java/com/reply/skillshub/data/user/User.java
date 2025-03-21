package com.reply.skillshub.data.user;

import java.util.ArrayList;
import java.util.List;

import com.reply.skillshub.data.hascertificate.HasCertificate;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.experience.Experience;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.skill.Skill;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    private String profilePictureLocation;
    
    @Email
    @NotEmpty
    private String email;

    private String phoneNumber;

    @NotNull
    private UserRole userRole;

    @NotEmpty
    private String password;

    private String confirmationToken;
    
    @NotNull
    private boolean confirmed;

    @Relationship(type = "SPEAKS")
    private List<Speaks> speaks = new ArrayList<>();

    @Relationship(type = "WORKS_FOR", cascadeUpdates = false)
    private List<Company> companies = new ArrayList<>();

    @Relationship(type = "HAS_EXPERIENCE", cascadeUpdates = false)
    private List<Experience> experiences = new ArrayList<>();

    @Relationship(type = "HAS_SKILL", cascadeUpdates = false)
    private List<Skill> skills = new ArrayList<>();

    @Relationship(type = "HAS_RESUME")
    private List<ShortCv> resumes = new ArrayList<>();

    @Relationship(type = "HAS_CERTIFICATE")
    private List<HasCertificate> hasCertificates = new ArrayList<>();

    public String getFullname() {
        return this.firstName + " " + this.lastName;
    }
}
