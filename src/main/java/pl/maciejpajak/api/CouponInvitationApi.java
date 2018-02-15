package pl.maciejpajak.api;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.api.dto.AcceptCouponInvitationDto;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.security.CurrentUser;
import pl.maciejpajak.service.CouponInvitationService;

@RestController
@RequestMapping("/coupon-invitations")
public class CouponInvitationApi {
    
    private final CouponInvitationService couponInvitationService;
    
    @Autowired
    public CouponInvitationApi(CouponInvitationService couponInvitationService) {
        this.couponInvitationService = couponInvitationService;
    }
    
//     TODO secure this - user can access only his invitations
    @GetMapping("/user/{couponId}")
    public CouponInvitation getCouponInvitation(Long id) {
        return couponInvitationService.findbyId(id);
    }
    
    @GetMapping("/user/pending")
    public Collection<CouponInvitation> getPendingCouponInvitations(@AuthenticationPrincipal CurrentUser principal) {
        return couponInvitationService.findAllPending(principal.getId());
    }
    
    @PostMapping("/user/accept")
    public ResponseEntity acceptCouponInvitation(@RequestBody @Valid AcceptCouponInvitationDto invitationDto, @AuthenticationPrincipal CurrentUser principal) {
        couponInvitationService.acceptCouponInvitation(invitationDto, principal.getId());
        return ResponseEntity.ok().build(); // TODO update this
    }

}
