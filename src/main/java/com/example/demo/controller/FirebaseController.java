package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.notification.NotificationRequest;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.FirebaseService;

@RestController
@RequestMapping("/api/firebase")
public class FirebaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(FirebaseController.class);
	
	@Autowired
	FirebaseService firebaseService;

    @PostMapping("/send")
    public HttpEntity<?> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        try {
            ApiResponse apiResponse = firebaseService.sendNotification(notificationRequest);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error send notification: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("Error send notification", false));
        }
    }
}
