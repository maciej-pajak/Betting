package pl.maciejpajak.testing;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.repository.RoleRepository;
import pl.maciejpajak.repository.UserRepository;
import pl.maciejpajak.security.Role;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;
   
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0) {
//        System.out.println("App has started!");
//        Role r = new Role();
//        r.setName("ROLE_USER");
//        r = roleRepo.save(r);
//        Set<Role> roles = new HashSet<>();
//        roles.add(r);
//        
//        BCryptPasswordEncoder e = new BCryptPasswordEncoder();
//        User u = new User();
//        u.setLogin("user");
//        u.setPassword(e.encode("user"));
//        u.setRoles(roles);
//        userRepo.save(u);
        
//        subRepo.findOne(1L);
        
    }

}
