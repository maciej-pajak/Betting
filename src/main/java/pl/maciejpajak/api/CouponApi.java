package pl.maciejpajak.api;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.dto.BidAmountBonusDto;
import pl.maciejpajak.dto.CouponPlaceDto;
import pl.maciejpajak.dto.CouponShowDto;
import pl.maciejpajak.dto.GroupCouponPlaceDto;
import pl.maciejpajak.security.CurrentUser;
import pl.maciejpajak.service.CouponService;

@RestController
@RequestMapping("/coupons")
public class CouponApi {
    
    private final CouponService couponService;
    
    public CouponApi(CouponService couponService) {
        this.couponService = couponService;
    }
    
    @GetMapping("/all-individual")
    public Collection<CouponShowDto> findAllIndividualCoupons(@AuthenticationPrincipal CurrentUser user) {
        return couponService.findAllIndividualCoupons(user.getId());
    }
    
    @GetMapping("/all-group")
    public Collection<CouponShowDto> findAllGroupCoupons(@AuthenticationPrincipal CurrentUser user) {
        return couponService.findAllGroupCoupons(user.getId());
    }
    
    @GetMapping("/all-group/owned-only")
    public Collection<CouponShowDto> findOwnedGroupCoupons(@AuthenticationPrincipal CurrentUser user) {
        return couponService.findOwnedGroupCoupons(user.getId());
    }
    
    @GetMapping("/all-group/invited-only")
    public Collection<CouponShowDto> findInvitedGroupCoupons(@AuthenticationPrincipal CurrentUser user) {
        return couponService.findInvitedGroupCoupons(user.getId());
    }

    @PostMapping("/create")
    public void createCoupon(@RequestBody @Valid CouponPlaceDto couponDto, @AuthenticationPrincipal CurrentUser principal) {
        couponService.createCoupon(couponDto, principal.getId());
    }
    
    @PostMapping("/group/create")
    public void createGroupCoupon(@RequestBody @Valid GroupCouponPlaceDto couponDto, @AuthenticationPrincipal CurrentUser principal) {
        couponService.createCoupon(couponDto, principal.getId());
    }
    
    @GetMapping("/bonuses")
    public Collection<BidAmountBonusDto> showAvailableBidAmountBonuses() {
        return couponService.findAllBidAmountBonuses();
    }

}
