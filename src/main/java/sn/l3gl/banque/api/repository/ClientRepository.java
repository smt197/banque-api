package sn.l3gl.banque.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.l3gl.banque.api.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
