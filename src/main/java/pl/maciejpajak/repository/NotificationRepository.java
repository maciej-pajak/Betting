package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    public Collection<Notification> findAllByUserIdAndReadOrderByCreatedDesc(Long userId, boolean isRead);

}
