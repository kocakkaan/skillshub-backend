package com.reply.skillshub.data.project.reference;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.Data;

@Node("ProjectReference")
@Data
public class ProjectReference {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String language;
    private String title;

    @Property("initial_situation")
    private List<String> initialSituation = new ArrayList<>();

    private List<String> challenges = new ArrayList<>();

    @Property("approach")
    private List<String> approachTechnologies = new ArrayList<>();

    @Property("value_added_1")
    private String valueAddedText0;
    @Property("value_added_2")
    private String valueAddedText1;
    @Property("value_added_3")
    private String valueAddedText2;

}
