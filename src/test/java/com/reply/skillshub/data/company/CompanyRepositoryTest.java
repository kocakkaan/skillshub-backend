package com.reply.skillshub.data.company;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.user.User;

@DataNeo4jTest
public class CompanyRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void succesfullySaveCompany() {
        Company company = new Company();
        company.setLabel("tester");
        company.getEmployees().add(returnUserWithEmail());
        Company savedCompany = companyRepository.save(company);
        Assertions.assertNotNull(savedCompany.getId());
    }

    @Test
    void test_findByEmployeesId() {
        Company company = new Company();
        User user = returnUserWithEmail();
        company.setLabel("tester");
        company.getEmployees().add(user);
        companyRepository.save(company);

        List<Company> companyList = companyRepository.findByEmployeesId(user.getId(), Company.class);

        Assertions.assertEquals(1, companyList.size());
    }

    User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }
    
}
