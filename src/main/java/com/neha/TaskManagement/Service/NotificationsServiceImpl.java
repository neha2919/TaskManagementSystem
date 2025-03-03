package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Entity.Notifications;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Repository.NotificationsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationsServiceImpl implements NotificationsService{

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public void sendNotifications(User user, String message) {
        Notifications notifications = new Notifications();
        notifications.setRecipient(user);
        notifications.setDescription(message);
        notifications.setTimestamp(LocalDateTime.now());
        notificationsRepository.save(notifications);
    }

    @Override
    public void markNotificationsAsSeen(UUID notificationsTd){
        Notifications notification = notificationsRepository.findById(notificationsTd)
                .orElseThrow(()->new EntityNotFoundException("Notification not found with ID: "+ notificationsTd));
        notification.setSeen(true);
        notificationsRepository.save(notification);
    }
}
