package com.reply.skillshub.data.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.neovisionaries.i18n.LanguageCode;

public interface UserRepository extends Neo4jRepository<User, String> {

    List<User> findByFirstName(String firstname);

    List<User> findByLastName(String lastname);

    <T> Optional<T> findByEmail(String email, Class<T> type);

    <T> Optional<T> findById(String id, Class<T> type);

    <T> Optional<T> findByConfirmationToken(String confirmationToken);

    boolean existsByEmail(String email);

    List<User> findBySpeaksLanguageLanguageCode(LanguageCode languageCode);

    List<Employee> findByCompaniesIdIn(List<String> company);

    <T> T findByResumesId(String resumeId, Class<T> type);

    
}