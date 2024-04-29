Feature: Liking a post
  Scenario: Pressing the like button
    Given a user has uploaded a post on the lecture page
    When the user has pressed the like button
    Then the like count of a post will update

  Scenario: Popular post must be attended to by lecturer
    Given a user has uploaded a post on the lecture page
    When the post receives more than 10 likes within the first minute
    Then notify the Lecturer of the post's popularity and prompt the Lecturer


