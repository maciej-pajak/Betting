package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findOneByName(String roleName);
    
}
