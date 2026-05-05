package sn.l3gl.banque.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.l3gl.banque.api.model.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
