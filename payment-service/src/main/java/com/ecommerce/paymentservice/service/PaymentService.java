package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for order: {}", request.getOrderId());

        try {
            // Create payment record
            Payment payment = new Payment();
            payment.setOrderId(request.getOrderId());
            payment.setUserId(request.getUserId());
            payment.setAmount(request.getAmount());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setTransactionId(generateTransactionId());

            // Simulate payment processing
            if (validatePayment(request)) {
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                log.info("Payment processed successfully: {}", payment.getTransactionId());
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                log.error("Payment validation failed for order: {}", request.getOrderId());
            }

            // Save payment
            payment = paymentRepository.save(payment);

            // Send notification email
            emailService.sendPaymentNotification(payment);

            return mapToResponse(payment);
        } catch (Exception e) {
            log.error("Error processing payment", e);
            throw new RuntimeException("Payment processing failed: " + e.getMessage());
        }
    }

    public PaymentResponse getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));
        return mapToResponse(payment);
    }

    private boolean validatePayment(PaymentRequest request) {
        // Basic validation - in production, integrate with payment gateway
        return request.getAmount().compareTo(BigDecimal.ZERO) > 0
                && request.getPaymentMethod() != null
                && !request.getPaymentMethod().isEmpty()
                && request.getOrderId() != null
                && request.getUserId() != null;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus().toString(),
                payment.getTransactionId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
