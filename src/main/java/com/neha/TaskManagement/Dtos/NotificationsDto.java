package com.neha.TaskManagement.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neha.TaskManagement.Entity.Notifications;
import com.neha.TaskManagement.Entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationsDto {
    private UUID notificationsId;
    @NotNull(message = "Please mention the description of the notification")
    private String description;
    private LocalDateTime timestamp;
    private User recipient;
    private boolean seen;

    public static Notifications dtoToEntity(NotificationsDto dto){
        Notifications entity = new Notifications();
        entity.setNotificationsTd(dto.getNotificationsId());
        entity.setDescription(dto.getDescription());
        entity.setTimestamp(dto.getTimestamp());
        entity.setRecipient(dto.getRecipient());
        entity.setSeen(dto.isSeen());

        return entity;
    }

    public static NotificationsDto entityToDto(Notifications entity){
        NotificationsDto dto = new NotificationsDto();
        dto.setNotificationsId(entity.getNotificationsTd());
        dto.setDescription(entity.getDescription());
        dto.setTimestamp(entity.getTimestamp());
        dto.setRecipient(entity.getRecipient());
        dto.setSeen(entity.isSeen());

        return dto;
    }
}
