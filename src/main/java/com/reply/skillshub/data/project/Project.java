package com.reply.skillshub.data.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.client.Client;
import com.reply.skillshub.data.contact.Contact;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.project.reference.ProjectReference;
import com.reply.skillshub.data.projecttype.ProjectType;
import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.user.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Node
public class Project {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotNull
  private int projectId;

  @NotEmpty
  private String title;

  private String description;

  private String company;

  @Relationship(type = "FOR_CLIENT", direction = Direction.OUTGOING)
  private List<Client> clients = new ArrayList<>();

  @Relationship(type = "HAS_CONTACT", direction = Direction.OUTGOING)
  private Contact contact;

  @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING)
  private Industry industry;

  @Relationship(type = "MANAGED_BY", direction = Direction.OUTGOING)
  private User manager;

  private boolean isPublic = false;

  private boolean isPublicSector = false;

  @Relationship(type = "HAS_PROJECT_TYPE", direction = Direction.OUTGOING)
  private List<ProjectType> projectType = new ArrayList<>();

  @Relationship(type = "WORKED_ON", direction = Direction.INCOMING)
  private List<User> users = new ArrayList<>();

  private LocalDate startDate;
  private LocalDate endDate;

  private double revenue;

  private ProjectStatus status;

  private String projectPictureLocation;

  @Relationship(type = "HAS_REFERENCE", direction = Direction.OUTGOING)
  private List<ProjectReference> references = new ArrayList<>();

  @Relationship(type = "USED_TECHNOLOGY", direction = Direction.OUTGOING)
  private List<Skill> technologies = new ArrayList<>();

  public String getFormattedProjectId() {
    return "ID" + String.format("%04d", this.projectId);
  }

  public Optional<ProjectReference> getReferenceByLanguage(String language) {
    return references.stream()
        .filter(reference -> reference.getLanguage().equalsIgnoreCase(language))
        .findFirst();
  }

}
