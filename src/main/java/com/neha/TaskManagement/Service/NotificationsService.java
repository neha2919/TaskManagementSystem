package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Entity.Notifications;
import com.neha.TaskManagement.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface NotificationsService {
    void sendNotifications(User user, String message);

    void markNotificationsAsSeen(UUID notificationsTd);
//    List<Notifications> getUnseenNotifications(User user);
}
