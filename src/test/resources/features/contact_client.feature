Feature: Contact and Client Management
  As an authenticated user
  I want to manage clients and their contacts
  So that I can track stakeholders linked to projects in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Create a new client
    When I submit POST to "/clients" with name "Acme Industries"
    Then the response status should be 200
    And the response body contains field "id"
    And the response body contains "Acme Industries"

  Scenario: Get all clients returns a list
    Given at least one client exists
    When I request GET "/clients"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Search clients by name
    Given a client with name "TechCorp" exists
    When I request GET "/clients?search=Tech"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Create a new contact
    When I submit POST to "/contacts" with name "Jane Doe" and email "jane.doe@client.com"
    Then the response status should be 200
    And the response body contains field "id"
    And the response body contains "Jane Doe"

  Scenario: Get all contacts returns a list
    Given at least one contact exists
    When I request GET "/contacts"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get a specific contact by ID
    Given a contact with name "John Smith" exists
    When I request GET "/contacts/{contactId}"
    Then the response status should be 200
    And the response body contains "John Smith"

  Scenario: Update a contact
    Given a contact with name "Old Name" exists
    When I submit PUT to "/contacts/{contactId}" with name "New Name" and email "new@client.com"
    Then the response status should be 200
    And the response body contains "New Name"

  Scenario: Search contacts returns matching results
    Given a contact with name "Alice Wonderland" exists
    When I request GET "/contacts?search=Alice"
    Then the response status should be 200
    And the response is a JSON array
