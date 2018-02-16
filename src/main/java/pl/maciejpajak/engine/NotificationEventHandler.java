package pl.maciejpajak.engine;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.notification.Notification;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.event.NotifyUserEvent;
import pl.maciejpajak.repository.NotificationRepository;

@Component
public class NotificationEventHandler {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationEventHandler.class);

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates new user notification and saves it in database.
     * @param event
     */
    @EventListener
    public void handleNotifyUserEvent(NotifyUserEvent event) {
        log.debug("Saving user (id = {}) notification (type = {})", event.getUserId(), event.getNotificationType());
        notificationRepository.save(
                Notification.builder()
                    .created(LocalDateTime.now())
                    .objectId(event.getObjectId())
                    .read(false)
                    .user(User.builder().id(event.getUserId()).build())
                    .objectId(event.getObjectId())
                    .message(event.getNotificationType().getMessage())
                    .build());
    }
    
}
