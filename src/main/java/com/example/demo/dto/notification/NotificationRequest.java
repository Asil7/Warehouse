package com.example.demo.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationRequest {
	private String userToken;
    private String title;
    private String body;
    private String route;
}
