package pl.maciejpajak.api.temp;

import java.math.BigDecimal;
import java.util.Collections;

import javax.management.relation.RoleNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.UserRegistrationDto;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.LoginOrEmailAlreadyTakenException;
import pl.maciejpajak.repository.RoleRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
public class RegistrationService {
    
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public void registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByLoginOrEmail(dto.getLogin(), dto.getEmail())) {
            throw new LoginOrEmailAlreadyTakenException();
        }
        try {
            userRepository.save(User.builder()
                    .email(dto.getEmail())
                    .login(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .roles( Collections.singleton(roleRepository.findOneByName("ROLE_USER")
                                .orElseThrow(() -> new RoleNotFoundException())
                            ))
                    .visible(true)
                    .balance(BigDecimal.valueOf(0))
                    .build());
        } catch (RoleNotFoundException e) {
            log.error("Role not found", e);
        }
        log.debug("Registered new user {}", dto.getEmail());
    }
    
}
