package pl.maciejpajak.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
