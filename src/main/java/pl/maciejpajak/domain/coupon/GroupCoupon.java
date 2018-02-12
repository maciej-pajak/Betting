package pl.maciejpajak.domain.coupon;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GroupCoupon extends UserCoupon {

    @OneToMany(mappedBy = "groupCoupon", cascade =CascadeType.PERSIST)
    private Set<CouponInvitation> intivations;
    
}
