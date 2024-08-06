// Requires Enterprise edition
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.firstName IS NOT NULL;
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.lastName IS NOT NULL;
// CREATE CONSTRAINT FOR (p:Person) REQUIRE p.email IS NOT NULL;

CREATE CONSTRAINT User_Email_Unique FOR (p:User) REQUIRE p.email IS UNIQUE;



