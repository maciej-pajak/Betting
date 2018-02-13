package pl.maciejpajak.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.repository.UserRepository;

@Component
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User u = userRepository.findOneByLoginOrEmailEquals(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("login/email not found"));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : u.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        System.out.println("======================");
        System.out.println(u.getPassword());
        System.out.println(u.getEmail());
        return new CurrentUser(u.getEmail(), u.getPassword(), grantedAuthorities, u.getId());
    }

}
