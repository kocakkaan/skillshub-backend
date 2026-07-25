Feature: User Profile Management
  As an authenticated user
  I want to view and update my professional profile
  So that I can maintain accurate information in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Get current user profile returns profile information
    When I request GET "/user/me/profile"
    Then the response status should be 200
    And the response body contains field "email"

  Scenario: Get user profile by ID returns profile information
    Given the current user ID is known
    When I request GET "/users/{userId}/profile"
    Then the response status should be 200
    And the response body contains field "email"

  Scenario: Update user profile successfully
    Given the current user ID is known
    When I submit a PUT request to "/users/{userId}/profile" with updated first name "UpdatedFirst"
    Then the response status should be 200
    And the response body contains "UpdatedFirst"

  Scenario: Get employees accessible to current user
    Given the admin user belongs to a company
    When I request GET "/user/me/employees"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Add a skill to a user
    Given a skill with label "Java" exists in the system
    When I submit POST to "/users/{userId}/skills/{skillId}"
    Then the response status should be 200
    And the response body contains field "id"

  Scenario: Remove a skill from a user
    Given the user has skill "Java" assigned
    When I submit DELETE to "/users/{userId}/skills/{skillId}"
    Then the response status should be 200

  Scenario: Add languages to a user
    When I submit PUT to "/users/{userId}/languages" with language code "de" and level "B2"
    Then the response status should be 200
    And the response is a JSON array
