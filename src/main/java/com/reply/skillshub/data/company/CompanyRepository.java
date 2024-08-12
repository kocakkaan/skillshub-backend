package com.reply.skillshub.data.company;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;
import com.reply.skillshub.data.user.User;


public interface CompanyRepository extends Neo4jRepository<Company, String> {

    List<Company> findByEmployeesId(String employeeId);
    
}
