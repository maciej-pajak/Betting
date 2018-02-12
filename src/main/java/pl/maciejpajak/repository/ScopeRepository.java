package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.Scope;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    
    public Collection<Scope> findAllByVisible(boolean isVisible);
    public Collection<Scope> findAllByCompetitonsSportIdAndVisible(Long sportId, boolean isVisible);

}
