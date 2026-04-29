package sn.l3gl.banque.api.mapper;

import org.springframework.stereotype.Component;
import sn.l3gl.banque.api.dto.TransactionResponse;
import sn.l3gl.banque.api.model.Compte;
import sn.l3gl.banque.api.model.Transaction;

@Component
public class TransactionMapper {

    /**
     * Mapper Transaction vers TransactionResponse
     */
    public TransactionResponse toResponse(Transaction transaction, Compte compte) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setMontant(transaction.getMontant());
        response.setDate(transaction.getDate());
        response.setType(transaction.getType().getLibelle());
        response.setNumeroCompte(compte.getNumero());
        response.setNouveauSolde(compte.getSolde());
        return response;
    }
}
