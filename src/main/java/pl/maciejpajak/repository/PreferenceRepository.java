package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    public Collection<Preference> findAllBySubscribedAndUserId(boolean isSubscribed, Long userId);
    public Optional<Preference> findOneByIdAndUserIdAndSubscribed(Long subscriptionId, Long userId, boolean isVisible);
    
}
