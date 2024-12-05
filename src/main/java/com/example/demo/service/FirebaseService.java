package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.notification.NotificationRequest;
import com.example.demo.payload.ApiResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseService {
	
    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    public ApiResponse sendNotification(NotificationRequest notificationRequest) {
        try {
        	
            String userToken = notificationRequest.getUserToken();
            String title = notificationRequest.getTitle();
            String body = notificationRequest.getBody();
            String route = notificationRequest.getRoute();
        	
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(userToken)
                    .setNotification(notification)
                    .putData("route", route)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Notification successfully sent: {}", response);

            return new ApiResponse("Xabar yuborildi", true);

        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage(), e);
            return new ApiResponse("Failed to send notification", false);
        }
    }
	
}
