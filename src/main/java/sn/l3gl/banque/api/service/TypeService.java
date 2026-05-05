package sn.l3gl.banque.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.l3gl.banque.api.model.Type;
import sn.l3gl.banque.api.repository.TypeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    public Optional<Type> findByLibelle(String libelle) {
        return typeRepository.findByLibelle(libelle);
    }

    public Type save(Type type) {
        return typeRepository.save(type);
    }
}
