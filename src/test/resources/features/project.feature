Feature: Project Management
  As an authenticated user
  I want to create and manage projects
  So that I can track client engagements in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Create a new project
    When I submit POST to "/projects" with title "Cloud Migration" and description "Moving workloads to AWS"
    Then the response status should be 200
    And the response body contains field "id"
    And the response body contains "Cloud Migration"

  Scenario: Get all projects returns a list
    Given at least one project exists
    When I request GET "/projects"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Search projects by keyword
    Given a project with title "Digital Transformation" exists
    When I request GET "/projects?search=Digital"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get project by ID
    Given a project with title "SAP Implementation" exists
    When I request GET "/projects/{projectId}"
    Then the response status should be 200
    And the response body contains "SAP Implementation"

  Scenario: Update an existing project
    Given a project with title "Legacy Modernisation" exists
    When I submit PUT to "/projects/{projectId}" with title "Legacy Modernisation Updated"
    Then the response status should be 200
    And the response body contains "Legacy Modernisation Updated"

  Scenario: Delete a project (admin only)
    Given a project with title "Project To Delete" exists
    When I submit DELETE to "/projects/{projectId}"
    Then the response status should be 200

  Scenario: Delete project returns 403 for non-admin user
    Given an authenticated employee user
    And a project exists
    When the employee submits DELETE to "/projects/{projectId}"
    Then the response status should be 403
