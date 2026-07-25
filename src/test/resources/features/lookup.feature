Feature: Lookup Data (Industries, Languages, Occupations)
  As an authenticated user
  I want to retrieve reference data
  So that I can populate dropdowns and filters in the SkillsHub frontend

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Get all industries returns a list
    When I request GET "/industries"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Search industries by label
    When I request GET "/industries?label=Finance"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get all languages returns a list
    When I request GET "/languages"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get all occupations returns a list
    When I request GET "/occupations"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Search occupations by label
    When I request GET "/occupations?label=Consultant"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Add a new occupation
    When I submit POST to "/occupations" with label "Cloud Architect"
    Then the response status should be 200
    And the response body contains "Cloud Architect"
    And the response body contains field "id"
