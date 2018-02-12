package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findOneByLoginOrEmailEquals(String login, String email); // TODO and visible = true
    Optional<User> findOneById(Long id); // TODO and visible = true
}
