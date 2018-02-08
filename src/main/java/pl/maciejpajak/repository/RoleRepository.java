package pl.maciejpajak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
