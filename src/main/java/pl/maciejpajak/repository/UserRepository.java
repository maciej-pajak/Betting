package pl.maciejpajak.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByLoginOrEmail(String login, String email);
    Optional<User> findOneByLoginOrEmailEquals(String login, String email); // TODO and visible = true
    Optional<User> findOneById(Long id); // TODO move to version with visble
    Optional<User> findOneByIdAndVisible(Long id, boolean isvisible);
    Stream<User> findAllByVisible(boolean isVisible);
}
