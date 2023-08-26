package com.picpay.picpaysimplified.service;

import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.dto.NotificationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.NotAcceptableStatusException;

@Log4j2
@Service
public class NotificationService {
    @Autowired
    RestTemplate restTemplate;

    public void sendNotification(User user, String message ) {
        NotificationDTO notificationDTO = new NotificationDTO(user.getEmail(), message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("http://o4d9z.mocklab.io/notify", notificationDTO, String.class);
        if (!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            log.warn("Error sending notification");
            throw new NotAcceptableStatusException("Notification service is out of sign");
        }
    }
}
