Feature: Liking a post
  Scenario: Two users have liked a comment
  Given that comment 1 has been posted
  And "User 1" has liked the comment
  And "User 2" has liked the comment
  When the page refreshes
  Then comment 1 has a like count of 2



