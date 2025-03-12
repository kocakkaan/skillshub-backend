package com.reply.skillshub.data.speaks;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.reply.skillshub.data.language.Language;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@RelationshipProperties
@Data
public class Speaks {

    @RelationshipId
    @GeneratedValue
    private String id;
    
    @TargetNode
    private Language language;

    @NotNull
    private boolean isNative;

    private LanguageLevel languageLevel;

    @AssertTrue
    boolean isNativeFalseLanguageLevelNotNull() {
        return isNative || (isNative == false && languageLevel != null);
    }

}
