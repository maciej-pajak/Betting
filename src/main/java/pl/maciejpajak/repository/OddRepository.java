package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Odd;

public interface OddRepository extends JpaRepository<Odd, Long> {

//    public Optional<Odd> findOneByBetOptionId(Long betOptionId);
    
    public Optional<Odd> findFirstByBetOptionIdOrderByCreatedDesc(Long betOptionId);
    
}
