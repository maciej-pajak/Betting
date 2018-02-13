package pl.maciejpajak;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.repository.UserRepository;

@SpringBootApplication
public class BettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}
	
	//TODO remove
	@Bean
    CommandLineRunner init(UserRepository userRepo) {
        return (evt) -> {
            User u = new User();
            u.setEmail("test@test.pl");
            u.setPassword(new BCryptPasswordEncoder().encode("test"));
            System.out.println(u.getPassword());
        };
    }
}
