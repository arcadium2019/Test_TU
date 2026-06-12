Feature: Account creation

  Scenario: Successful account creation
    When the user registers with username "alice", email "alice@test.com" and password "pass123"
    Then the account is created successfully

  Scenario: Account creation fails with duplicate username
    Given a user with username "alice" already exists
    When the user registers with username "alice", email "alice2@test.com" and password "pass456"
    Then an error is returned with message "Username already exists"
