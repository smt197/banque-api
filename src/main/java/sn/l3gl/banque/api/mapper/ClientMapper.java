package sn.l3gl.banque.api.mapper;

import org.springframework.stereotype.Component;
import sn.l3gl.banque.api.dto.ClientResponse;
import sn.l3gl.banque.api.dto.CreateCompteNewRequest;
import sn.l3gl.banque.api.model.Client;

@Component
public class ClientMapper {

    /**
     * Mapper CreateCompteNewRequest vers Client (nouveau client)
     */
    public Client toEntity(CreateCompteNewRequest request) {
        Client client = new Client();
        client.setNumPiece(request.getNumPiece());
        client.setPrenom(request.getPrenom());
        client.setNom(request.getNom());
        client.setTelephone(request.getTelephone());
        client.setAdresse(request.getAdresse());
        client.setDateNaissance(request.getDateNaissance());
        return client;
    }

    /**
     * Mapper Client vers ClientResponse
     */
    public ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setNumPiece(client.getNumPiece());
        response.setPrenom(client.getPrenom());
        response.setNom(client.getNom());
        response.setAdresse(client.getAdresse());
        response.setDateNaissance(client.getDateNaissance());
        response.setTelephone(client.getTelephone());
        return response;
    }
}
