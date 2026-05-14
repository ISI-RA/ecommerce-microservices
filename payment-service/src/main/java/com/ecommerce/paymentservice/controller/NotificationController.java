package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestParam Long userId,
            @RequestParam String subject,
            @RequestBody String body) {
        log.info("Sending email to user: {}", userId);
        emailService.sendOrderConfirmation(userId, userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sms")
    public ResponseEntity<Map<String, String>> sendSMS(
            @RequestParam Long userId,
            @RequestParam String message) {
        log.info("SMS notification sent to user: {}", userId);
        // Integrate with SMS provider here
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "SMS sent successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
