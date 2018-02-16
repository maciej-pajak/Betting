package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.dto.SubscriptionDto;
import pl.maciejpajak.security.CurrentUser;
import pl.maciejpajak.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionApi {
    
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/all")
    public Collection<SubscriptionDto> getUserPartySubscriptions(@AuthenticationPrincipal CurrentUser principal) {
        return subscriptionService.findUserSubscriptions(principal.getId());
    }
    
    @PostMapping("/subscribe/competition")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void subscribeCompetition(@RequestParam(name = "competitionId", required = true) Long competitionId,
                        @AuthenticationPrincipal CurrentUser principal) {
        subscriptionService.subscribeCompetition(principal.getId(), competitionId);
    }
    
    @PostMapping("/subscribe/party")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void subscribeParty(@RequestParam(name = "partyId", required = true) Long partyId,
                        @AuthenticationPrincipal CurrentUser principal) {
        subscriptionService.subscribeParty(principal.getId(), partyId);
    }
    
    @DeleteMapping("/unsubscribe")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void unsubscribe(@RequestParam(name = "subscriptionId", required = true) Long subscriptionId,
                        @AuthenticationPrincipal CurrentUser principal) {
        subscriptionService.unsubscribe(principal.getId(), subscriptionId);
    }
    
}
