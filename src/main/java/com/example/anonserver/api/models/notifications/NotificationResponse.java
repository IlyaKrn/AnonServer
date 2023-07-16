package com.example.anonserver.api.models.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse implements Serializable {

    private NotificationType type;
    private Object data;

}
