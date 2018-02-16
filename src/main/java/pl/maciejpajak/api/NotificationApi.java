package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.notification.Notification;
import pl.maciejpajak.security.CurrentUser;
import pl.maciejpajak.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationApi {

    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/unread")
    public Collection<Notification> getAllUnread(@AuthenticationPrincipal CurrentUser principal) {
        return notificationService.getUsersUnreadNotifications(principal.getId());
    }
}
