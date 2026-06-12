Feature: Login

  Scenario: Successful login
    Given a user with username "alice" and password "pass123" exists
    When the user logs in with username "alice" and password "pass123"
    Then the login succeeds

  Scenario: Login fails with wrong password
    Given a user with username "alice" and password "pass123" exists
    When the user logs in with username "alice" and password "wrongpass"
    Then an error is returned with message "Invalid credentials"
