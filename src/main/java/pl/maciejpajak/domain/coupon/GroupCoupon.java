package pl.maciejpajak.domain.coupon;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class GroupCoupon extends Coupon {

    @OneToMany(mappedBy = "groupCoupon")
    private Set<CouponInvitation> intivations;
    
}
