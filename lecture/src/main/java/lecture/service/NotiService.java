package lecture.service;

import lecture.domain.Notification;
import lecture.domain.User;

import java.util.ArrayList;
import java.util.List;

public class NotiService {
    private final List<Notification> notificationPool;
    private NotiListener listener;

    public NotiService() {
        this.notificationPool = new ArrayList<>();
    }

    public void setNotificationListener(NotiListener listener) {
        this.listener = listener;
    }

    public void notifyUser(User user, String message) {
        Notification notification = new Notification(message, user);
        notificationPool.add(notification);
        if (listener != null) {
            listener.onNotiReceived(notification);
        }
    }

    public void broadcastNotification(List<User> users, String message) {
        for (User user : users) {
            notifyUser(user, message);
        }
    }

    private void displayNotification(Notification notification) {
        System.out.println("Notification for " + notification.getRecipient().getName() + ": " + notification.getMessage());
    }
}
