package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.maciejpajak.domain.bet.Odd;

public interface OddRepository extends JpaRepository<Odd, Long> {

    public Optional<Odd> findOneById(Long betOptionId);
//    @Query("SELECT o FROM odd WHERE o.bet_option_id=:betOptionId ORDER BY o.created DESC LIMIT 1")
    // TODO optimize this query so its executed without join
    public Optional<Odd> findFirstByBetOptionIdOrderByCreatedDesc(Long betOptionId);
    
}
