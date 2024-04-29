package lecture.util;

import lecture.domain.User;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }
}
