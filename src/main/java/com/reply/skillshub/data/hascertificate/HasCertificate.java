package com.reply.skillshub.data.hascertificate;

import com.reply.skillshub.data.certificate.Certificate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDate;

@RelationshipProperties
@Data
public class HasCertificate {  
    
    @RelationshipId
    private Long id;

    @TargetNode
    private Certificate certificate;

    @NotNull
    private LocalDate issuedDate;

    private LocalDate expirationDate;

    private String file;

}
