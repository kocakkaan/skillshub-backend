package com.reply.skillshub.data.industry;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.neo4j.core.Neo4jClient;

import com.reply.skillshub.BaseRepositoryTest;

@DataNeo4jTest
public class IndustryRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private IndustryRepository industryRepository;

  @Test
  void succesfullySaveIndustry() {
    Industry industry = new Industry();
    Industry savedIndustry = industryRepository.save(industry);
    assertNotNull(savedIndustry.getId());
  }

  @Test
  void test_findAllByIndustriesId(@Autowired Neo4jClient client) {
    client.query("create (re:Resume {id: \"testResume\", title: \"SomeResume\"})\n" + //
            "create (i:Industry {label: \"TestIndustry\"})\n" + //
            "create (re) - [:IN_INDUSTRY] -> (i);").run();
    var resultingIndustries = industryRepository.findAllByResumeId("testResume");
    assertEquals(1, resultingIndustries.size());
  }
  
}
