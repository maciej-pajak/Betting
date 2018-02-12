package pl.maciejpajak.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.api.dto.BetOptionWithOddDto;
import pl.maciejpajak.api.dto.PlacedCouponDto;
import pl.maciejpajak.api.dto.PlacedGroupCouponDto;
import pl.maciejpajak.api.temp.CouponService;
import pl.maciejpajak.domain.coupon.UserCoupon;

@RestController
@RequestMapping("/coupons")
public class CouponApi {
    
    private final CouponService couponService;
    
    public CouponApi(CouponService couponService) {
        this.couponService = couponService;
    }

    // TODO remove
    @GetMapping("/test")
    public PlacedCouponDto test() {
        PlacedCouponDto c = new PlacedCouponDto();
        c.setAmount(BigDecimal.valueOf(100.33));
        c.setOddsChangeAccepted(true);
        
        
        List<BetOptionWithOddDto> list = new ArrayList<>();
        BetOptionWithOddDto b1 = new BetOptionWithOddDto();
        b1.setBetOptionId(1L);
        b1.setBetOptionId(2L);
        list.add(b1);
        c.setBetOptionsWithOdds(list);
        return c;
    }
    
    @GetMapping("/all")
    public Collection<UserCoupon> findAllCoupons() {
        return couponService.findAllForCurrentUser();
    }
    
    @PostMapping("/create")
    public void createCoupon(@RequestBody @Valid PlacedCouponDto couponDto) {
        couponService.createCoupon(couponDto);
    }
    
    @PostMapping("/group/create")
    public void createGroupCoupon(@RequestBody @Valid PlacedGroupCouponDto couponDto) {
        couponService.createCoupon(couponDto);
    }

}