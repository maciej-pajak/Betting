package pl.maciejpajak.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.repository.UserRepository;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepo;
    
    public User registerUser() {
        return userRepo.save(new User());
    }
}
