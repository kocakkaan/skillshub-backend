package com.reply.skillshub.data.company;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends Neo4jRepository<Company, String> {

    <T> List<T> findByEmployeesId(String employeeId, Class<T> type);

    <T> Optional<T> findById(String id, Class<T> type);

}
