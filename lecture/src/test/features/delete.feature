Feature: Deleting a Post or Comment
  Scenario: User deletes their own post
    Given a user "Alice" makes a post on the lecture page
    When "Alice" presses the delete button on their post
    Then the post made by "Alice" is deleted

  Scenario: Flagging an inappropriate post for deletion
    Given a lecturer "Bob" identifies a post as inappropriate on the lecture page
    When "Bob" flags the post as inappropriate
    Then the flagged post is deleted

  Scenario: Lecturer deletes a specified comment
    Given the user "Charlie" is a lecturer
    And the page has comments "hello", "goodbye", and "wtf" with respective likes 4, 2, and 10
    And "Charlie" selects the comment "wtf"
    When "Charlie" presses the delete button on the selected comment
    Then there will be 2 comments left
    And the comment "wtf" will not be there
