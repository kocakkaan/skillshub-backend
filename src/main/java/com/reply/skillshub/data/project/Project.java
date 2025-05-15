package com.reply.skillshub.data.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.reply.skillshub.data.client.Client;
import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.user.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Node
public class Project {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotEmpty
  private String title;

  private String company;

  @Relationship(type = "FOR_CLIENT", direction = Direction.OUTGOING)
  private Client client;

  @Relationship(type = "IN_INDUSTRY", direction = Direction.OUTGOING)
  private Industry industry;

  private boolean isPublic;

  private String projectType;

  @Relationship(type = "WORKED_ON", direction = Direction.INCOMING)
  private List<User> users;

  private LocalDate startDate;
  private LocalDate endDate;

  private ProjectStatus status;

  private List<String> initialSituation = new ArrayList<>();
  private List<String> challenges = new ArrayList<>();
  private List<String> approachTechnologies = new ArrayList<>();
  private List<String> technologies = new ArrayList<>();
  private String valueAddedText0;
  private String valueAddedText1;
  private String valueAddedText2;

  private String projectPictureLocation;

}
