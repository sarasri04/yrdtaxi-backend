
package com.yrdtaxi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    private static final Logger log =
            LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${whatsapp.api-url:https://graph.facebook.com}")
    private String apiUrl;

    @Value("${whatsapp.api-version:v23.0}")
    private String apiVersion;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.admin-number}")
    private String adminNumber;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean sendAdminBookingMessage(
            Long bookingId,
            String customerName,
            String customerPhone,
            String pickup,
            String drop,
            String tripType,
            double totalFare,
            String paymentStatus) {

        try {

            // 1. Validate configuration
            if (accessToken == null || accessToken.isBlank()
                    || phoneNumberId == null || phoneNumberId.isBlank()
                    || adminNumber == null || adminNumber.isBlank()) {

                log.error("WhatsApp configuration is missing.");
                return false;
            }

            // 2. Build API URL
            String url = apiUrl.replaceAll("/+$", "")
                    + "/" + apiVersion.replaceAll("^/+", "")
                    + "/" + phoneNumberId.trim()
                    + "/messages";

            // 3. Create booking notification
            String message = """
                    YRD TAXI - NEW BOOKING

                    Booking ID: %s
                    Customer: %s
                    Mobile: %s
                    Pickup: %s
                    Drop: %s
                    Trip: %s
                    Total Fare: Rs. %.2f
                    Payment Status: %s

                    Please check the YRD TAXI admin dashboard.
                    """.formatted(
                    bookingId,
                    safe(customerName),
                    safe(customerPhone),
                    safe(pickup),
                    safe(drop),
                    safe(tripType),
                    totalFare,
                    safe(paymentStatus)
            );

            // 4. WhatsApp request body
            Map<String, Object> body = new HashMap<>();

            body.put("messaging_product", "whatsapp");
            body.put("recipient_type", "individual");
            body.put("to", adminNumber.trim().replaceAll("[^0-9]", ""));
            body.put("type", "text");

            body.put("text", Map.of(
                    "preview_url", false,
                    "body", message
            ));

            // 5. Authorization headers
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(accessToken.trim());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(
                    java.util.List.of(MediaType.APPLICATION_JSON)
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            // 6. Send message
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            request,
                            String.class
                    );

            log.info(
                    "WhatsApp notification successful. Booking: {}, HTTP: {}, Response: {}",
                    bookingId,
                    response.getStatusCode(),
                    response.getBody()
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (RestClientResponseException e) {

            log.error(
                    "WhatsApp API failed. Booking: {}, HTTP: {}, Response: {}",
                    bookingId,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );

            return false;

        } catch (Exception e) {

            log.error(
                    "WhatsApp notification error for booking {}: {}",
                    bookingId,
                    e.getMessage(),
                    e
            );

            return false;
        }
    }

    private String safe(String value) {

        return value == null || value.isBlank()
                ? "Not provided"
                : value;
    }
}


