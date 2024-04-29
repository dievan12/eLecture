package lecture;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import junit.framework.Assert;

import java.util.HashMap;
import java.util.Map;
import lecture.domain.*;
import lecture.service.NotiService;

public class LikeTest {
    private final Page page;
    private final Map<Integer, Comment> comments;
    private final Map<String, User> users;

    public LikeTest() {
        this.page = new Page(new NotiService());
        this.comments = new HashMap<>();
        this.users = new HashMap<>();
    }

    @Given("that comment {int} has been posted")
    public void thatCommentHasBeenPosted(int commentId) {
        // For demonstration, assume all comments are posted by a generic Student user
        User user = this.users.computeIfAbsent("GenericUser", name -> new Student(1, name, "student", "hashed_password"));
        Comment comment = new Comment(commentId, "Sample comment", user, 0);
        this.comments.put(commentId, comment);
        this.page.addComment(comment);
    }

    @And("{string} has liked the comment")
    public void hasLikedTheComment(String userName) {
        this.users.computeIfAbsent(userName, name -> new Student(1, name, "student", "hashed_password"));
        Comment lastComment = this.comments.values().iterator().next();
        lastComment.addLike();
    }

    @When("the page refreshes")
    public void thePageRefreshes() {
    }

    @Then("comment {int} has a like count of {int}")
    public void commentHasALikeCountOf(Integer commentId, Integer expectedLikes) {
        Comment comment = this.comments.get(commentId);
        Assert.assertNotNull("Comment should exist", comment);
        Assert.assertEquals("Like count should match expected value", expectedLikes.intValue(), comment.getLikes());
    }
}
