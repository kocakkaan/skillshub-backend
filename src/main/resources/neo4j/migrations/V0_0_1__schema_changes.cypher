// Requires Enterprise edition
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.firstName IS NOT NULL;
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.lastName IS NOT NULL;
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.email IS NOT NULL;

CREATE CONSTRAINT person_Email_Unique FOR (p:Person) REQUIRE p.email IS UNIQUE;



