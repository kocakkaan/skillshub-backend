package com.reply.skillshub.data.language;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.neovisionaries.i18n.LanguageCode;

public interface LanguageRepository extends Neo4jRepository<Language, LanguageCode> {
  
  
}
