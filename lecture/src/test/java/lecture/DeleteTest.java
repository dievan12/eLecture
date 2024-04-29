package lecture;

import io.cucumber.java.en.*;
import junit.framework.Assert;
import lecture.domain.*;
import lecture.service.NotiService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteTest {
    private final Map<String, User> users = new HashMap<>();
    private final Page page;
    private final Map<String, Post> posts = new HashMap<>();
    private final Map<Integer, Comment> comments = new HashMap<>();
    private Integer selectedCommentId = null;
    private Lecturer lecturer;
    private Post inappropriatePost;
    private final AtomicInteger nextCommentId = new AtomicInteger(1);
    private final AtomicInteger userIdCounter = new AtomicInteger(1);


    private int nextUserId() {
        return userIdCounter.getAndIncrement();
    }

    public DeleteTest() {
        this.page = new Page(new NotiService());
    }

    @Given("a user {string} makes a post on the lecture page")
    public void aUserMakesAPostOnTheLecturePage(String userName) {
        User user = users.computeIfAbsent(userName, name -> new Student(nextUserId(), name, "student", "hashed_password"));
        Post post = new Post("Sample post content", user,false);
        this.page.addPost(post);
        this.posts.put(userName, post);
    }

    @When("{string} presses the delete button on their post")
    public void theUserPressesTheDeleteButtonOnTheirPost(String userName) {
        User user = users.get(userName);
        Post post = posts.get(userName);
        boolean deleted = this.page.deletePost(user, post.getId());
        Assert.assertTrue("Post should be deleted", deleted);
    }

    @Then("the post made by {string} is deleted")
    public void thePostMadeByTheUserIsDeleted(String userName) {
        Assert.assertTrue("The post should be deleted",
                posts.get(userName) == null || !this.page.getPosts().contains(posts.get(userName)));
    }


    @Given("the page has comments with content {string}, {string}, and {string}")
    public void thePageHasCommentsWithContent(String comment1, String comment2, String comment3) {
        int id = nextCommentId.getAndIncrement(); // Assuming this provides a unique integer ID
        User user = new Student(id, "Commenter", "student", "hashed_password"); // Add role and password assuming these are needed for Student

        // Instantiate comments using the correct id for the user
        Comment c1 = new Comment(id, comment1, user, 0);
        Comment c2 = new Comment(id, comment2, user,  0);
        Comment c3 = new Comment(id, comment3, user, 0);

        this.page.addComment(c1);
        this.page.addComment(c2);
        this.page.addComment(c3);

        comments.put(c1.getCommentID(), c1);
        comments.put(c2.getCommentID(), c2);
        comments.put(c3.getCommentID(), c3);
    }

    @When("{string} presses the delete button on the selected comment")
    public void pressesTheDeleteButtonOnTheSelectedComment(String userName) {
        User user = this.users.get(userName);
        if (selectedCommentId != null) {
            boolean deleted = this.page.deleteComment(user, selectedCommentId);
            Assert.assertTrue("Comment should be deleted", deleted);
        }
    }

    @Then("the selected comment is no longer present")
    public void theSelectedCommentIsNoLongerPresent() {
        Assert.assertTrue("The selected comment should no longer be present",
                selectedCommentId == null || !comments.containsKey(selectedCommentId));
    }

    @And("{string} selects the comment {string}")
    public void selectsTheComment(String userName, String commentContent) {
        this.selectedCommentId = comments.values().stream()
                .filter(comment -> commentContent.equals(comment.getContent()))
                .map(Comment::getCommentID)
                .findFirst()
                .orElse(null);
    }


    @Given("a lecturer {string} identifies a post as inappropriate on the lecture page")
    public void aLecturerIdentifiesAPostAsInappropriateOnTheLecturePage(String lecturerName) {
        this.lecturer = (Lecturer) users.get(lecturerName);
        // Assuming that there is a post to be marked as inappropriate
        this.inappropriatePost = this.page.getPosts().stream().findFirst().orElse(null);
    }

    @When("the lecturer flags the post as inappropriate")
    public void theLecturerFlagsThePostAsInappropriate() {
        if (this.inappropriatePost != null) {
            this.page.markPostAsInappropriate(this.inappropriatePost.getId(), this.lecturer);
        }
    }

    @Then("the flagged post is deleted")
    public void theFlaggedPostIsDeleted() {
        Assert.assertFalse("The flagged post should be deleted",
                this.page.getPosts().contains(this.inappropriatePost));
    }

    @When("{string} flags the post as inappropriate")
    public void flagsThePostAsInappropriate(String lecturerName) {
        User lecturer = users.get(lecturerName);
        if (lecturer instanceof Lecturer && !page.getPosts().isEmpty()) {
            Post postToFlag = page.getPosts().getFirst();
            page.markPostAsInappropriate(postToFlag.getId(), lecturer);
            this.inappropriatePost = postToFlag;
        }
    }
    @And("the page has comments {string}, {string}, and {string} with respective likes {int}, {int}, and {int}")
    public void thePageHasCommentsWithRespectiveLikes(String firstComment, String secondComment, String thirdComment, int firstLikes, int secondLikes, int thirdLikes) {
        int studentId = nextUserId();
        String role = "student";
        String password = "hashed_password";

        User defaultUser = new Student(studentId, "DefaultUser", role, password);
        Comment c1 = new Comment(nextCommentId.getAndIncrement(), firstComment, defaultUser, firstLikes);
        Comment c2 = new Comment(nextCommentId.getAndIncrement(), secondComment, defaultUser, secondLikes);
        Comment c3 = new Comment(nextCommentId.getAndIncrement(), thirdComment, defaultUser, thirdLikes);

        page.addComment(c1);
        page.addComment(c2);
        page.addComment(c3);

        comments.put(c1.getCommentID(), c1);
        comments.put(c2.getCommentID(), c2);
        comments.put(c3.getCommentID(), c3);
    }

    @Then("there will be {int} comments left")
    public void thereWillBeCertainNumberOfCommentsLeft(int expectedCommentCount) {
        int actualCommentCount = page.getComments().size();
        Assert.assertEquals("The number of comments left should match the expected count.", expectedCommentCount, actualCommentCount);
    }

    @And("the comment {string} will not be there")
    public void theCommentWillNotBePresent(String missingCommentContent) {
        boolean commentExists = page.getComments().stream()
                .anyMatch(comment -> missingCommentContent.equals(comment.getContent()));
        Assert.assertFalse("The comment should not exist on the page.", commentExists);
    }

    @Given("the user {string} is a lecturer")
    public void theUserIsALecturer(String userName) {
        int id = nextCommentId.getAndIncrement();
        String role = "Lecturer";
        String password = "hashed_password_placeholder";
        Lecturer user = new Lecturer(id, userName, role, password);
        this.users.put(userName, user);
        this.lecturer = user;
    }
}
