@profile-lifecycle.update-long-address
Feature: Profile updates

  # OpenSpec: profile-lifecycle.update-long-address
  Scenario: Update a profile with a 300-character street address
    Given I create a profile for user "Fin"
    When I update the profile with a street address with 300 characters
    Then the response status should be 200
