package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendPaymentNotification(Payment payment) {
        try {
            String status = payment.getStatus().toString();
            String subject = "Payment " + status + " - Order #" + payment.getOrderId();
            String body = buildPaymentEmailBody(payment);

            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(getEmailForUser(payment.getUserId()));
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Payment notification email sent for order: {}", payment.getOrderId());
            } else {
                log.warn("Email service not configured, skipping email notification");
                log.info("Would send email: {} - {}", subject, body);
            }
        } catch (Exception e) {
            log.error("Error sending payment notification email", e);
        }
    }

    public void sendOrderConfirmation(Long orderId, Long userId) {
        try {
            String subject = "Order Confirmation #" + orderId;
            String body = "Your order #" + orderId + " has been confirmed. Thank you for your purchase!";

            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(getEmailForUser(userId));
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Order confirmation email sent for order: {}", orderId);
            } else {
                log.warn("Would send order confirmation: {} - {}", subject, body);
            }
        } catch (Exception e) {
            log.error("Error sending order confirmation email", e);
        }
    }

    private String buildPaymentEmailBody(Payment payment) {
        return String.format(
                "Payment Status: %s\n\n" +
                "Order ID: %d\n" +
                "Amount: $%.2f\n" +
                "Transaction ID: %s\n" +
                "Payment Method: %s\n" +
                "Date: %s\n\n" +
                "Thank you for your business!",
                payment.getStatus(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getTransactionId(),
                payment.getPaymentMethod(),
                payment.getCreatedAt()
        );
    }

    private String getEmailForUser(Long userId) {
        // In production, call user service to get email
        return "user" + userId + "@example.com";
    }
}
