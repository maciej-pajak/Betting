package pl.maciejpajak.testing.event.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class NotifyUserEvent extends ApplicationEvent {

    private Long userId;
    private Long objectId;
    private NotificationType notificationType;
    
    public NotifyUserEvent(Object source, Long userId, Long objectId, NotificationType notificationType) {
        super(source);
        this.userId = userId;
        this.objectId = objectId;
        this.notificationType = notificationType;
    }

}
