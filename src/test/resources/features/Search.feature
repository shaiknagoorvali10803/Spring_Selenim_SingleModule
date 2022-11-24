@chrome
Feature: Google search

  Background:
    When I am Google Page
    Then Search for the Word "Nagoor"

  @google
  Scenario Outline: I want to search on google site
    Given I am on the google site
    When I enter "<keyword>" as a keyword
    Then I should see search results page
    Then I should see at least <count> results

    Examples:
      | keyword  | count |
      | selenium | 10    |
      | java     | 8     |
      | spring   | 1     |