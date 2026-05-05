package sn.l3gl.banque.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.l3gl.banque.api.model.Compte;
import sn.l3gl.banque.api.repository.CompteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompteService {

    private final CompteRepository compteRepository;

    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    public Optional<Compte> findByNumero(String numero) {
        return compteRepository.findByNumero(numero);
    }

    public List<Compte> findByClientId(Long clientId) {
        return compteRepository.findByClientId(clientId);
    }

    public List<Compte> findAll() {
        return compteRepository.findAll();
    }
}
