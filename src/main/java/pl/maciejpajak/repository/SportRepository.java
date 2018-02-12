package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.Sport;

public interface SportRepository extends JpaRepository<Sport, Long>{
    
    public Collection<Sport> findAllByVisible(boolean isVisible);

}
