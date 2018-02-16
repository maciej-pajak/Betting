package pl.maciejpajak.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.notification.Notification;
import pl.maciejpajak.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    public Collection<Notification> getUsersUnreadNotifications(Long userId) {
        return notificationRepository.findAllByUserIdAndReadOrderByCreatedDesc(userId, false);
    }
    
}
