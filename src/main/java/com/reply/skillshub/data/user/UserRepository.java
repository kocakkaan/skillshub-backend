package com.reply.skillshub.data.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.neovisionaries.i18n.LanguageCode;
import com.reply.skillshub.data.userrole.UserRole;

public interface UserRepository extends Neo4jRepository<User, String> {

        List<User> findByFirstName(String firstname);

        List<User> findByLastName(String lastname);

        <T> Optional<T> findByEmail(String email, Class<T> type);

        <T> Optional<T> findById(String id, Class<T> type);

        <T> Optional<T> findByConfirmationToken(String confirmationToken, Class<T> type);

        boolean existsByEmail(String email);

        /**
         * Returns the first User node matching the given role.
         * Used by the signup flow to find the super/root admin that acts
         * as the {@code createdBy} reference for newly signed-up users.
         */
        Optional<User> findFirstByUserRole(UserRole userRole);

        List<User> findBySpeaksLanguageLanguageCode(LanguageCode languageCode);

        List<Employee> findByCompaniesIdIn(List<String> company);

        <T> T findByResumesId(String resumeId, Class<T> type);

        @Query("""
                        match (u:User) - [r:WORKS_FOR] -> (c:Company {id: $companyId})
                        optional match (u) - [h_s:HAS_SKILL] -> (skill:Skill where toLower(skill.label) in $skills)
                        with u, collect(skill) as skills
                        optional match (u) - [h_c:HAS_CERTIFICATE] -> (cert:Certificate where toLower(cert.name) in $certificates)
                        with u, skills, collect(cert) as certs
                        where size(skills) > 0 or size(certs) > 0
                        return u
                        """)
        List<Employee> findByCompaniesIdAndSkillsLabelInOrHasCertificatesCertificateNameIn(String companyId,
                        List<String> skills, List<String> certificates);

        @Query("""
                        match (u:User) - [r:WORKS_FOR] -> (c:Company where c.id in $companyId)
                        optional match (u) - [h_s:HAS_SKILL] -> (skill:Skill where toLower(skill.label) in $skills)
                        with u, collect(skill) as skills
                        optional match (u) - [h_c:HAS_CERTIFICATE] -> (cert:Certificate where toLower(cert.name) in $certificates)
                        with u, skills, collect(cert) as certs
                        where size(skills) > 0 or size(certs) > 0
                        return u
                        """)
        List<Employee> findByCompaniesIdInAndSkillsLabelInOrHasCertificatesCertificateNameIn(List<String> companyId,
                        List<String> skills, List<String> certificates);

}
