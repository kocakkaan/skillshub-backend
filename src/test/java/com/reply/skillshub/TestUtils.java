package com.reply.skillshub;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.util.StreamUtils;

public class TestUtils {

  public static void runScript(Neo4jClient client, String path) throws IOException {
    ClassPathResource resource = new ClassPathResource(path);
    String cypherQuery = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    client.query(cypherQuery).run();
  }

}
