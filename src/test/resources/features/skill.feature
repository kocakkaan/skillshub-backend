Feature: Skill Management
  As an authenticated user
  I want to search and manage skills
  So that I can tag competencies across profiles in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Get all skills returns a list
    Given at least one skill exists in the system
    When I request GET "/skills"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Search skills by label
    Given a skill with label "Python" exists in the system
    When I request GET "/skills?label=Python"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Add a new skill to the system
    When I submit POST to "/skills" with label "Kubernetes"
    Then the response status should be 200
    And the response body contains "Kubernetes"
    And the response body contains field "id"

  Scenario: Adding a skill returns its ID for further use
    When I submit POST to "/skills" with label "Docker"
    Then the response status should be 200
    And the response body contains field "id"
