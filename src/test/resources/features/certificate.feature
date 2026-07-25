Feature: Certificate Management
  As an authenticated user
  I want to manage professional certifications
  So that I can showcase qualifications in SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Add a certificate to a user
    When I submit POST to "/users/{userId}/certificates" with name "AWS Solutions Architect" and issuer "Amazon"
    Then the response status should be 200
    And the response body contains "AWS Solutions Architect"

  Scenario: Get all certificates for a user
    Given the user has at least one certificate
    When I request GET "/users/{userId}/certificates"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Delete a certificate from a user
    Given the user has a certificate assigned
    When I submit DELETE to "/users/{userId}/certificates/{certificateId}"
    Then the response status should be 200
