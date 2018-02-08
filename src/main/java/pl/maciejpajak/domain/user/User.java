package pl.maciejpajak.domain.user;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.security.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @Column(unique = true)
    private String login;
    
    @NotBlank
    private String password;
    
//    @OneToOne(optional = false) // TODO
//    private Wallet wallet;
    
//    @ManyToMany
//    private Set<Friends> friends;
    
//    @ManyToMany
//    private Set<PrivateMessage> messages;
    
    @OneToMany(mappedBy = "owner")
    private Set<Coupon> coupons;
    
    @OneToMany(mappedBy = "invitedUser")
    private Set<CouponInvitation> couponInvitations;
    
    @ManyToMany
    private Set<Role> roles;
    
    
    // getters & setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
    
}
