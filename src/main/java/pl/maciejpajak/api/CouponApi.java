package pl.maciejpajak.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.api.dto.CouponDto;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.OddRepository;

@RestController
@RequestMapping("/coupons")
public class CouponApi {
    
    private final CouponRepository couponRepository;
    private final OddRepository oddRepository;
    
    public CouponApi(CouponRepository couponRepository, OddRepository oddRepository) {
        this.couponRepository = couponRepository;
        this.oddRepository = oddRepository;
    }

    @PostMapping("/create")
    public void createCoupon(@RequestBody @Valid CouponDto couponDto) {
        couponDto.getAmount();
        couponDto.getBets();
        couponDto.isOddsChangeAccepted();
    }

}
