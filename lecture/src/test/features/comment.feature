Feature: Comment Management
  As a user, I want to manage comments on posts to interact with other users.

  Scenario: Student posts a comment on a post
    Given I am logged in as a "student" with id 1, username "StudentName", role "student", and password "student1"
    And there is an existing post
    When I comment "Great post!" on the post
    Then the comment "Great post!" should be visible on the post

  Scenario: Student deletes a comment
    Given I am logged in as a "student" with id 1, username "StudentName", role "student", and password "student1"
    And there is an existing post
    And I have commented "Great post!" on a post
    When I delete my comment from the post
    Then the comment should no longer be visible on the post

  Scenario: Lecturer posts a comment on a post
    Given I am logged in as a "lecturer" with id 1, username "LecturerName", role "lecturer", and password "lecturer1"
    And there is an existing post
    When I comment "Excellent analysis!" on the post
    Then the comment "Excellent analysis!" should be visible on the post

  Scenario: Lecturer deletes a comment
    Given I am logged in as a "lecturer" with id 1, username "LecturerName", role "lecturer", and password "lecturer1"
    And there is an existing post
    And I have commented "Excellent analysis!" on a post
    When I delete my comment from the post
    Then the comment should no longer be visible on the post


