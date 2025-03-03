package com.neha.TaskManagement.Repository;

import com.neha.TaskManagement.Entity.Notifications;
import com.neha.TaskManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
    List<Notifications> findByRecipient(User recipient);
//    List<Notifications> findByRecipientAndSeenFalse(User recipient); //filter only unread notifications
}
