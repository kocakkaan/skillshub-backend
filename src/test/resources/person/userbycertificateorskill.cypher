// Create a company
create (c:Company {id: "1", name: "Company 1"})

// Create users
create (u:User {id: "1", email: "user1"})
create (u1:User {id: "2", email: "user2"})
create (u2:User {id: "3", email: "user3"})

// Create a user with a company
create (u)-[:WORKS_FOR]->(c)
create (u1)-[:WORKS_FOR]->(c)
create (u2)-[:WORKS_FOR]->(c)


// Create skills
create (s:Skill {id: "1", label: "Java"})
create (s1:Skill {id: "2", label: "Spring"})
create (s2:Skill {id: "3", label: "Hibernate"})

// Create certificates
create (c1:Certificate {id: "1", name: "Certificate 1"})
create (c2:Certificate {id: "2", name: "Certificate 2"})
create (c3:Certificate {id: "3", name: "Hibernate Core"})

// User has two skills
create (u)-[:HAS_SKILL]->(s)
create (u)-[:HAS_SKILL]->(s1)
create (u)-[:HAS_CERTIFICATE]->(c2)

// User one has two skill and two certificates
create (u1)-[:HAS_SKILL]->(s)
create (u1)-[:HAS_SKILL]->(s2)
create (u1)-[:HAS_CERTIFICATE]->(c1)
create (u1)-[:HAS_CERTIFICATE]->(c3)

// User two has one skill and one certificate
CREATE (u2)-[:HAS_SKILL]->(s)
create (u2)-[:HAS_SKILL]->(s2)
create (u2)-[:HAS_CERTIFICATE]->(c1)
create (u2)-[:HAS_CERTIFICATE]->(c3)