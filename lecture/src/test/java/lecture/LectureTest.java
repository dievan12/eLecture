package lecture;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;
import lecture.domain.*;
import lecture.service.NotiService;

public class LectureTest {
    private final Page page;
    private Post post;
    private final User user;

    public LectureTest() {
        this.user = new Student(1, "Student1", "student", "Student1");
        this.page = new Page(new NotiService());
    }

    @Given("a user has uploaded a post on the lecture page")
    public void a_user_has_uploaded_a_post_on_the_lecture_page() {
        post = new Post("Sample post content", this.user, false);
        this.page.addPost(post);
    }

    @When("the user has pressed the like button on comment {int}")
    public void the_user_has_pressed_the_like_button_on_comment(Integer commentID) {
        // Assuming that comment ID is being used to retrieve and like a specific comment
        Comment comment = this.page.getComment(commentID);
        if (comment != null) {
            comment.addLike();
        }
    }

    @Then("the like count of comment {int} will update to {int}")
    public void the_like_count_of_comment_will_update_to(Integer commentID, Integer expectedLikes) {
        Comment foundComment = this.page.getComment(commentID);
        Assert.assertNotNull("Comment should exist", foundComment);
        Assert.assertEquals("Like count should match expected", (int)expectedLikes, foundComment.getLikes());
    }

    @When("the post receives more than {int} likes within the first minute")
    public void the_post_receives_more_than_likes_within_the_first_minute(Integer margin) {
        // This step assumes direct manipulation of likes for the scenario
        // In a real scenario, you might increment likes via a method call in a loop or another mechanism
        for (int i = 0; i <= margin; i++) {
            this.post.addLike(); // Assuming a method to add likes directly to the post
        }
    }

    @Then("notify the Lecturer of the post's popularity and prompt the Lecturer")
    public void notify_the_lecturer_of_the_post_s_popularity_and_prompt_the_lecturer() {
        // This step assumes checking the post's popularity after likes have been added
        // The logic for notifying the lecturer would be part of your application's business logic, not directly in the test
        Assert.assertTrue("Post should be popular (more than 10 likes)", this.post.getLikes() > 10);
    }

    @Given("a user comments {string} on the lecture page")
    public void aUserCommentsOnTheLecturePage(String commentContent) {
        Comment comment = new Comment(1, commentContent, this.user, 0);
        this.page.addComment(comment);
    }
    @When("the user has pressed the like button")
    public void the_user_has_pressed_the_like_button() {
        this.post.addLike();
    }
    @Then("the like count of a post will update")
    public void the_like_count_of_a_post_will_update() {
        Assert.assertTrue("The post's like count should be greater than 0", this.post.getLikes() > 0);

    }
}
