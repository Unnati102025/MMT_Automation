Feature: Book a one-way flight on MakeMyTrip

  Background:
    Given User navigates to the MakeMyTrip website

  Scenario Outline: Search and select the cheapest one-way flight from <source> to <destination>
    When User selects a "One Way" trip
    And User enters source city as "<source>"
    And User enters destination city as "<destination>"
    And User selects departure date as day "<day>", month "<month>" and year "<year>"
    And User clicks the search button
    And User sorts the flights by "Price" from low to high
    And User selects the cheapest flight
    Then User verifies the booking details for from city "<source>", to city "<destination>", and date "<day> <month> <year>" are correct on the review page

    Examples:
      | source     | destination | day | month  | year |
      | Chandigarh | Bengaluru   | 22  | August | 2025 |