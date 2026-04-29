package sn.l3gl.banque.api.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.l3gl.banque.api.dto.CompteDetailResponse;
import sn.l3gl.banque.api.dto.CompteResponse;
import sn.l3gl.banque.api.dto.CreateCompteRequest;
import sn.l3gl.banque.api.model.Client;
import sn.l3gl.banque.api.model.Compte;

@Component
@RequiredArgsConstructor
public class CompteMapper {

    private final ClientMapper clientMapper;

    /**
     * Mapper CreateCompteRequest vers Compte
     */
    public Compte toEntity(CreateCompteRequest request, Client client) {
        Compte compte = new Compte();
        compte.setSolde(request.getSolde());
        compte.setDateCreation(request.getDateCreation());
        compte.setClient(client);
        return compte;
    }

    /**
     * Mapper Compte vers CompteResponse (infos compte seulement)
     */
    public CompteResponse toResponse(Compte compte) {
        CompteResponse response = new CompteResponse();
        response.setId(compte.getId());
        response.setNumero(compte.getNumero());
        response.setSolde(compte.getSolde());
        response.setDateCreation(compte.getDateCreation());
        return response;
    }

    /**
     * Mapper Compte vers CompteDetailResponse (infos compte + client)
     */
    public CompteDetailResponse toDetailResponse(Compte compte) {
        CompteDetailResponse response = new CompteDetailResponse();
        response.setId(compte.getId());
        response.setNumero(compte.getNumero());
        response.setSolde(compte.getSolde());
        response.setDateCreation(compte.getDateCreation());

        if (compte.getClient() != null) {
            response.setClient(clientMapper.toResponse(compte.getClient()));
        }

        return response;
    }
}
