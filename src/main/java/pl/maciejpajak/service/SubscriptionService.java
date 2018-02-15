package pl.maciejpajak.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.CompetitionSubscriptionDto;
import pl.maciejpajak.api.dto.PartySubscriptionDto;
import pl.maciejpajak.api.dto.SubscriptionDto;
import pl.maciejpajak.domain.game.Competition;
import pl.maciejpajak.domain.game.PlayingParty;
import pl.maciejpajak.domain.user.CompetitionPreference;
import pl.maciejpajak.domain.user.PartyPreference;
import pl.maciejpajak.domain.user.Preference;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.CompetitionPreferenceRepository;
import pl.maciejpajak.repository.PartyPreferenceRepository;
import pl.maciejpajak.repository.PreferenceRepository;

@Service
public class SubscriptionService {
    
    @Autowired
    private PreferenceRepository preferenceRepository;
    
    @Autowired
    private CompetitionPreferenceRepository competitionPreferenceRepository;
    
    @Autowired
    private PartyPreferenceRepository partyPreferenceRepository;
    
    public Collection<SubscriptionDto> findUserSubscriptions(Long userId) {
        return preferenceRepository.findAllBySubsribedAndUserId(true, userId).stream().map(pref -> {
            if (pref instanceof PartyPreference) {
                return PartySubscriptionDto.builder()
                        .id(pref.getId())
                        .partyId(((PartyPreference) pref).getPlayingParty().getId())
                        .partyName(((PartyPreference) pref).getPlayingParty().getName())
                        .build();
            } else {
                return CompetitionSubscriptionDto.builder()
                        .id(pref.getId())
                        .competitionId(((CompetitionPreference) pref).getCompetition().getId())
                        .competitionName(((CompetitionPreference) pref).getCompetition().getName())
                        .build();
            }
        }).collect(Collectors.toList());
    }
    
    public void subscribeCompetition(Long userId, Long competitionId) {
        CompetitionPreference cp = competitionPreferenceRepository.findOneByCompetitionIdAndUserId(competitionId, userId)
            .orElse(CompetitionPreference.builder()
                        .user(User.builder().id(userId).build())
                        .competition(Competition.builder().id(competitionId).build())
                        .build());
        cp.setSubsribed(true);
        competitionPreferenceRepository.save(cp);
    }
    
    public void subscribeParty(Long userId, Long partyId) {
        PartyPreference cp = partyPreferenceRepository.findOneByPartyIdAndUserId(partyId, userId)
            .orElse(PartyPreference.builder()
                        .user(User.builder().id(userId).build())
                        .playingParty(PlayingParty.builder().id(partyId).build())
                        .build());
        cp.setSubsribed(true);
        partyPreferenceRepository.save(cp);
    }
    
    public void unsubscribe(Long userId, Long subscriptionId) {
        Preference pref = preferenceRepository.findOneByIdAndUserIdAndSubsribed(subscriptionId, userId, true)
                                .orElseThrow(() -> new BaseEntityNotFoundException(subscriptionId));
        pref.setSubsribed(false);
        preferenceRepository.save(pref);
    }

}
