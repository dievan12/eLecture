package lecture.service;

import lecture.domain.Notification;

public interface NotiListener {
    void onNotiReceived(Notification notification);
}
