package com.reply.skillshub.data.person;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.util.StreamUtils;

import com.neovisionaries.i18n.LanguageCode;
import com.reply.skillshub.BaseRepositoryTest;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.language.Language;
import com.reply.skillshub.data.speaks.Speaks;
import com.reply.skillshub.data.user.Employee;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserRepository;

@DataNeo4jTest
public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSuccesfullSave() {
        User savedPerson = userRepository.save(returnUserWithEmail());

        Assertions.assertThat(savedPerson.getId()).isNotNull();
    }

    @Test
    void throwsErrorWhenEmailNotUnique() {
        userRepository.save(returnUserWithEmail());
        Assertions
            .assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(() -> userRepository.save(returnUserWithEmail()));
    }

    @Test
    void testPersonWithEmailAndLanguage() {
        User person = returnUserWithEmail();
        person.getSpeaks().add(returnSpeaks());
        userRepository.save(person);

        Optional<User> foundPerson = userRepository.findByEmail(person.getEmail(), User.class);
        Assertions.assertThat(foundPerson.get().getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    void testPersonWithEmailAndWithoutLanguage() {
        User person = returnUserWithEmail();
        userRepository.save(person);

        Optional<User> foundPerson = userRepository.findByEmail(person.getEmail(), User.class);
        Assertions.assertThat(foundPerson.get().getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    void testFindByLanguagesLanguageCode() {
        User person = returnUserWithEmail();
        Language language = returnLanguage();
        Speaks speaks = new Speaks();
        speaks.setLanguage(language);
        speaks.setNative(true);
        person.getSpeaks().add(speaks);
        userRepository.save(person);

        List<User> foundList = userRepository.findBySpeaksLanguageLanguageCode(language.getLanguageCode());
        
        Assertions.assertThat(foundList.size()).isEqualTo(1);
    }

    @Test
    void testFindByEmail() {
        User userToSave = returnUserWithEmail();
        userRepository.save(userToSave);

        Optional<User> foundUser = userRepository.findByEmail(userToSave.getEmail(), User.class);

        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo(userToSave.getEmail());
    }

    @Test
    void testFindByCompanyIdInIdList() {
        Company company = Instancio.of(Company.class).set(field(Company::getEmployees), List.of()).create();
        Company company2 = Instancio.of(Company.class).set(field(Company::getEmployees), List.of()).create();

        User userOneToSave = returnUserWithEmail();
        userOneToSave.getCompanies().add(company);
        User userTwoToSave = returnUserWithEmailAndLanguage();
        userTwoToSave.getCompanies().add(company2);

        userRepository.save(userOneToSave);
        userRepository.save(userTwoToSave);

        List<Employee> foundList = userRepository.findByCompaniesIdIn(List.of(company.getId(), company2.getId())); 
        Assertions.assertThat(foundList.size()).isEqualTo(2);
    }

    @Test
    void testFindByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn(@Autowired Neo4jClient client) throws IOException {
        ClassPathResource resource = new ClassPathResource("person/userbycertificateorskill.cypher");
        String cypherQuery = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        client.query(cypherQuery).run();

        List<Employee> javaList = userRepository.findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn("1", List.of("java"), List.of("java"));
        Assertions.assertThat(javaList.size()).isEqualTo(3);

        List<Employee> hibernateList = userRepository.findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn("1", List.of("hibernate"), List.of("hibernate"));
        Assertions.assertThat(hibernateList.size()).isEqualTo(2);

        var list = List.of("hibernate", "certificate 2");

        List<Employee> certAndHibernateList = userRepository.findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn("1", list, list);
        Assertions.assertThat(certAndHibernateList.size()).isEqualTo(3);

        list = List.of("hibernate", "certificate 1");

        List<Employee> certAndHibernateList2 = userRepository.findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn("1", list, list);
        Assertions.assertThat(certAndHibernateList2.size()).isEqualTo(2);
    }

    User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }

    User returnUserWithEmailAndLanguage() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName1");
        personToSave1.setLastName("LastName1");
        personToSave1.setEmail("maurits.de.roover1@reply.com");
        personToSave1.getSpeaks().add(returnSpeaks());
        return personToSave1;
    }

    Speaks returnSpeaks() {
        Speaks speaks = new Speaks();
        speaks.setLanguage(returnLanguage());
        speaks.setNative(true);
        return speaks;
    }

    Language returnLanguage() {
        Language language = new Language();
        LanguageCode languageCode = LanguageCode.de;
        language.setLanguageCode(languageCode);
        language.setLanguageAlpha3Code(languageCode.getAlpha3());
        return language;
    }

    Company returnCompany() {
        Company company = new Company();
        company.setLabel("myLabel");
        return company;
    }


    
}
