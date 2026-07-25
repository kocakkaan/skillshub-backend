Feature: Company Management
  As an authenticated user
  I want to manage companies and their employees
  So that I can organise teams within SkillsHub

  Background:
    Given a confirmed admin user is authenticated

  Scenario: Create a new company
    When I submit POST to "/user/me/company" with label "Acme Corp"
    Then the response status should be 200
    And the response body contains field "id"

  Scenario: Get companies for current user
    Given the admin user belongs to a company
    When I request GET "/user/me/company"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Get company information by ID
    Given a company with label "Test Corp" exists
    When I request GET "/company/{companyId}"
    Then the response status should be 200
    And the response body contains "Test Corp"

  Scenario: Get employees of a company
    Given a company exists with the admin user as employee
    When I request GET "/company/{companyId}/employees"
    Then the response status should be 200
    And the response is a JSON array

  Scenario: Add new employee to a company
    Given a company exists with the admin user as employee
    When I submit POST to "/company/{companyId}/employees" with a valid CreateUserRequest
    Then the response status should be 201
    And the response body contains field "id"

  Scenario: Get companies returns 404 when user has no companies
    Given a confirmed admin user with no company affiliations
    When I request GET "/user/me/company"
    Then the response status should be 404
