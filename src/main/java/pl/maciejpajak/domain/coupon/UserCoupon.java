package pl.maciejpajak.domain.coupon;

import javax.persistence.Entity;

@Entity
//@Polymorphism(type = PolymorphismType.EXPLICIT)
//@Inheritance(strategy = InheritanceType.JOINED)
////@DiscriminatorColumn(name = "coupon_type")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class UserCoupon extends Coupon {

}
