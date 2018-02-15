package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.PartyPreference;

public interface PartyPreferenceRepository extends JpaRepository<PartyPreference, Long> {

    public Optional<PartyPreference> findOneByPartyIdAndUserId(Long partyId, Long userId);
    
}
