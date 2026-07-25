Feature: Resume (Short CV) Management
  As an authenticated user
  I want to create and manage resumes
  So that I can present professional profiles from SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Create a resume for the current user
    When I submit POST to "/user/me/resumes" with title "My Main CV"
    Then the response status should be 200
    And the response body contains field "id"

  Scenario: Get resumes for the current user
    Given the current user has at least one resume
    When I request GET "/user/me/resumes"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Create a resume for a specific user
    When I submit POST to "/users/{userId}/resumes" with title "Employee CV"
    Then the response status should be 200
    And the response body contains "Employee CV"

  Scenario: Get resumes for a specific user
    Given the user has at least one resume
    When I request GET "/users/{userId}/resumes"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get resume by ID
    Given a resume with title "Specific Resume" exists
    When I request GET "/resumes/{resumeId}"
    Then the response status should be 200
    And the response body contains "Specific Resume"

  Scenario: Update resume title
    Given a resume exists
    When I submit PUT to "/resumes/{resumeId}/title" with title "Updated Title"
    Then the response status should be 200
    And the response body contains "Updated Title"

  Scenario: Update resume role
    Given a resume exists
    When I submit PUT to "/resumes/{resumeId}/role" with role "Senior Consultant"
    Then the response status should be 200

  Scenario: Update resume background
    Given a resume exists
    When I submit PATCH to "/resumes/{resumeId}/background" with background "Expert in cloud technologies"
    Then the response status should be 200

  Scenario: Add an experience to a resume
    Given a resume exists and an experience exists for the user
    When I submit POST to "/resumes/{resumeId}/experiences" with the experience ID
    Then the response status should be 200

  Scenario: Get experiences linked to a resume
    Given a resume has at least one experience linked
    When I request GET "/resumes/{resumeId}/experiences"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get resume skills
    Given a resume exists
    When I request GET "/resumes/{resumeId}/skills"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Update resume skills
    Given a resume exists and a skill exists
    When I submit PUT to "/resumes/{resumeId}/skills" with skill list
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Delete a resume
    Given a resume exists
    When I submit DELETE to "/resumes/{resumeId}"
    Then the response status should be 200
