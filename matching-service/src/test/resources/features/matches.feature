Feature: Matches

  # Blank cells and omitted characteristics are unspecified.
  # Music genres can be supplied as a comma-separated list, e.g. ROCK, JAZZ.
  # OpenSpec: matching-workflow.emit-compatible-match
  Scenario: Matches can be found
    Given profile "user1" exists with these characteristics
      | characteristic | personal | preference | dislike |
      | gender         | MAN      | WOMAN      | X       |
      | hairColor      | BROWN    | BLOND      |         |
      | pets           | true     | true       |         |
    And profile "user2" exists with these characteristics
      | characteristic | personal | preference | dislike |
      | gender         | WOMAN    | MAN        | X       |
      | hairColor      | BLOND    | BROWN      |         |
      | pets           | true     | true       |         |
    When the matches are calculated
    Then the following matches are published to kafka
      | personA | personB |
      | user1   | user2   |
