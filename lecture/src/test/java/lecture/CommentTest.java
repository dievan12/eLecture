package lecture;

import io.cucumber.java.en.And;
import junit.framework.Assert;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import lecture.domain.*;

public class CommentTest {

    private lecture.domain.Post post;
    private lecture.domain.User user;

    private Integer lastID = 0;

    @Given("I am logged in as a {string} with id {int}, username {string}, role {string}, and password {string}")
    public void i_am_logged_in_as_a(String userType, int id, String userName, String role, String password) {
        if ("student".equalsIgnoreCase(userType)) {
            this.user = new Student(id, userName, "student", password);
        } else if ("lecturer".equalsIgnoreCase(userType)) {
            this.user = new Lecturer(id, userName, role, password);
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }


    @Given("there is an existing post")
    public void there_is_an_existing_post() {

        User author = this.user;
        this.post = new Post("Content of the post", author, false);
    }

    @When("I comment {string} on the post")
    public void i_comment_on_the_post(String commentContent) {
        lastID++;
        Comment newComment = new Comment(lastID, commentContent, this.user, 0);
        this.post.addComment(newComment);
    }

    @Then("the comment {string} should be visible on the post")
    public void the_comment_should_be_visible_on_the_post(String commentContent) {
        Comment expected = this.post.getComments().stream()
                .filter(c -> c.getCommentID().equals(this.lastID))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull("Comment should not be null", expected);
        Assert.assertEquals("Comment content should match", commentContent, expected.getContent());
    }

    @When("I delete my comment from the post")
    public void i_delete_my_comment_from_the_post() {
        this.post.deleteComment(this.lastID);
    }

    @Then("the comment should no longer be visible on the post")
    public void the_comment_should_no_longer_be_visible_on_the_post() {
        boolean existing = this.post.getComments().stream()
                .anyMatch(c -> c.getCommentID().equals(this.lastID));
        Assert.assertFalse(existing);
    }

    @And("I have commented {string} on a post")
    public void iHaveCommentedOnAPost(String commentContent) {
        lastID++;
        Comment comment = new Comment(lastID, commentContent, this.user, 0);
        this.post.addComment(comment);
    }


}
