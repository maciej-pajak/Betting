package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.api.temp.CouponInvitationService;
import pl.maciejpajak.domain.coupon.CouponInvitation;

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
    
    @GetMapping("/user/pending") // TODO add current user
    public Collection<CouponInvitation> getPendingCouponInvitations() {
        return couponInvitationService.findAllPending();
    }
    
    // TODO check access
    @PostMapping("/user/accept/{couponId}")
    public ResponseEntity acceptCouponInvitation(Long couponId) {
        Long userId = 1L;   // TODO change to current user
        
        return ResponseEntity.ok().build(); // TODO update this
    }

}
