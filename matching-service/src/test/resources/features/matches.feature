Feature: Matches

  Scenario: Matches can be found
    Given these profiles exist
      | username | gender | pref_gender | dislike_gender |
      | user1    | MAN    | WOMAN       | X              |
      | user2    | WOMAN  | MAN         | X              |
    When the matches are calculated
    Then the following matches are published to kafka
      | personA | personB |
      | user1   | user2   |
