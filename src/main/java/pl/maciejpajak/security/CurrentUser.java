package pl.maciejpajak.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import pl.maciejpajak.domain.user.User;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    
    private Long id;
    
    public CurrentUser(String username, String password,
            Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, true, true, true, true, authorities);
        this.id = userId;
    }
    
//    public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired,
//            boolean credentialsNonExpired, boolean accountNonLocked,
//            Collection<? extends GrantedAuthority> authorities,
//            Long id) {
//        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//        
//        System.out.println("Creating new CurrentUser"); // TODO remove
//    }

    public Long getId() {
        return this.id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
