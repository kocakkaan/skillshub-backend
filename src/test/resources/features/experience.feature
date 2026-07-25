Feature: Experience Management
  As an authenticated user
  I want to manage professional experiences
  So that I can build an accurate work history in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Create an experience for a user
    When I submit POST to "/users/{userId}/experiences" with title "Software Engineer"
    Then the response status should be 200
    And the response body contains field "id"
    And the response body contains "Software Engineer"

  Scenario: Get experiences for a user
    Given the user has at least one experience
    When I request GET "/users/{userId}/experiences"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get a specific experience by ID
    Given an experience with title "Tech Lead" exists for the user
    When I request GET "/experiences/{experienceId}"
    Then the response status should be 200
    And the response body contains "Tech Lead"

  Scenario: Update an experience
    Given an experience with title "Junior Developer" exists for the user
    When I submit PUT to "/experiences/{experienceId}" with title "Senior Developer"
    Then the response status should be 200
    And the response body contains "Senior Developer"

  Scenario: Delete an experience
    Given an experience exists for the user
    When I submit DELETE to "/experiences/{experienceId}"
    Then the response status should be 200
