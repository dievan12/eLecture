package lecture.domain;


public class Comment {
    private final Integer commentID;
    private final String content;
    private final User user; // Reference to the User who made the comment
    private int likes;

    public Comment(Integer commentID, String content, User user, int likes) {
        this.commentID = commentID;
        this.content = content;
        this.user = user;
        this.likes = likes;
    }

    public Integer getCommentID() {
        return commentID;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public int getLikes() {
        return likes;
    }

    public void addLike() {
        this.likes++;
    }
}
