package pl.maciejpajak.domain.user;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.offers.SpecialOffer;
import pl.maciejpajak.security.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    private boolean visible = true;
    
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @Column(unique = true)
    private String login;
    
    @NotBlank
    private String password;
    
//    @ManyToMany
//    private Set<Friends> friends;
    
//    @ManyToMany
//    private Set<PrivateMessage> messages;
    
    @OneToMany(mappedBy = "owner")
    private Set<UserCoupon> coupons;
    
    @OneToMany(mappedBy = "invitedUser")
    private Set<CouponInvitation> couponInvitations;
    
    @ManyToMany
    private Set<Role> roles;
    
    @OneToMany(mappedBy = "user")
    private Set<SpecialOffer> specialOffers;
    
    private BigDecimal balance;
    
    @OneToMany(mappedBy = "owner")
    private Set<Transaction> transactions;
    
}
