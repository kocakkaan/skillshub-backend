Feature: Authentication
  As a user
  I want to register, confirm my account, and log in
  So that I can securely access the SkillsHub platform

  Scenario: Successful user signup with matching passwords
    Given a new user with first name "Smoke", last name "User", email "newuser@reply.com"
    And the user provides matching passwords "TestPass123!" and "TestPass123!"
    When the user submits the signup request
    Then the response status should be 200

  Scenario: Signup fails when passwords do not match
    Given a new user with first name "Smoke", last name "User", email "mismatch@reply.com"
    And the user provides mismatched passwords "TestPass123!" and "WrongPass456!"
    When the user submits the signup request
    Then the response status should be 400

  Scenario: Signup fails when email is missing
    Given a signup request without an email field
    When the user submits the signup request
    Then the response status should be 400

  Scenario: Signup fails when first name is missing
    Given a signup request without a first name
    When the user submits the signup request
    Then the response status should be 400

  Scenario: Signup fails when last name is missing
    Given a signup request without a last name
    When the user submits the signup request
    Then the response status should be 400

  Scenario: Login with valid credentials returns JWT token
    Given a confirmed admin user exists with email "smoketest.admin@reply.com" and password "SmokeTest123!"
    When the user submits login credentials for "smoketest.admin@reply.com" with password "SmokeTest123!"
    Then the response status should be 200
    And the response contains a non-empty JWT token

  Scenario: Login fails for unconfirmed user
    Given an unconfirmed user exists with email "unconfirmed@reply.com"
    When the user submits login credentials for "unconfirmed@reply.com" with password "SomePass123!"
    Then the response status should be 401

  Scenario: Login fails with wrong password
    Given a confirmed admin user exists with email "smoketest.admin@reply.com" and password "SmokeTest123!"
    When the user submits login credentials for "smoketest.admin@reply.com" with password "WrongPassword!"
    Then the response status should be 401

  Scenario: Email confirmation with valid token confirms the user account
    Given a user exists with a known confirmation token "valid-confirm-token-abc123"
    When the confirmation endpoint is called with token "valid-confirm-token-abc123"
    Then the response status should be 200
