package sn.l3gl.banque.api.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sn.l3gl.banque.api.dto.*;
import sn.l3gl.banque.api.exception.BusinessException;
import sn.l3gl.banque.api.exception.ResourceNotFoundException;
import sn.l3gl.banque.api.mapper.ClientMapper;
import sn.l3gl.banque.api.mapper.CompteMapper;
import sn.l3gl.banque.api.mapper.TransactionMapper;
import sn.l3gl.banque.api.model.Client;
import sn.l3gl.banque.api.model.Compte;
import sn.l3gl.banque.api.model.Transaction;
import sn.l3gl.banque.api.model.Type;
import sn.l3gl.banque.api.service.ClientService;
import sn.l3gl.banque.api.service.CompteService;
import sn.l3gl.banque.api.service.TransactionService;
import sn.l3gl.banque.api.service.TypeService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private final CompteService compteService;
    private final ClientService clientService;
    private final TransactionService transactionService;
    private final TypeService typeService;
    private final CompteMapper compteMapper;
    private final ClientMapper clientMapper;
    private final TransactionMapper transactionMapper;

    /**
     * Orchestrer la création d'un compte bancaire.
     */
    @Transactional
    public CompteDetailResponse saveCompte(CreateCompteRequest request) {
        Client client;

        if (request instanceof CreateCompteExistingRequest existingRequest) {
            client = clientService.findById(existingRequest.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client avec l'id " + existingRequest.getClientId() + " introuvable !"));
        } else if (request instanceof CreateCompteNewRequest newRequest) {
            client = clientMapper.toEntity(newRequest);
            client = clientService.save(client);
        } else {
            throw new BusinessException("Type de requête de création de compte non supporté !");
        }

        Compte compte = compteMapper.toEntity(request, client);
        String generatedNumero = "CE-" + (int)(Math.random() * 90000000 + 10000000);
        compte.setNumero(generatedNumero);
        compte = compteService.save(compte);

        Type depotType = getOrCreateType("DEPOT");
        Transaction transaction = new Transaction();
        transaction.setMontant(request.getSolde());
        transaction.setDate(request.getDateCreation());
        transaction.setType(depotType);
        transaction.setCompte(compte);
        transactionService.save(transaction);

        return compteMapper.toDetailResponse(compte);
    }

    /**
     * Effectuer une transaction.
     */
    @Transactional
    public TransactionResponse handleTransaction(String numeroCompte, TransactionRequest request) {
        Compte compte = compteService.findByNumero(numeroCompte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte avec le numéro '" + numeroCompte + "' introuvable !"));

        String typeStr = request.getTypeTransaction().toUpperCase();

        if ("DEPOT".equals(typeStr)) {
            if (request.getMontant() < 10000) {
                throw new BusinessException("Le montant du dépôt doit être d'au moins 10 000 !");
            }
            compte.setSolde(compte.getSolde() + request.getMontant());

        } else if ("RETRAIT".equals(typeStr)) {
            long soldeApresRetrait = compte.getSolde() - request.getMontant();
            if (soldeApresRetrait < 50000) {
                long maxRetrait = compte.getSolde() - 50000;
                if (maxRetrait <= 0) {
                    throw new BusinessException(
                            "Retrait impossible ! Le solde actuel (" + compte.getSolde()
                                    + ") ne permet aucun retrait. Le solde minimum autorisé est 50 000.");
                }
                throw new BusinessException(
                        "Retrait impossible ! Le montant maximum que vous pouvez retirer est "
                                + maxRetrait + ". Le solde doit rester d'au moins 50 000.");
            }
            compte.setSolde(soldeApresRetrait);

        } else {
            throw new BusinessException("Type de transaction invalide ! Utilisez 'DEPOT' ou 'RETRAIT'.");
        }

        compteService.save(compte);

        Type type = getOrCreateType(typeStr);
        Transaction transaction = new Transaction();
        transaction.setMontant(request.getMontant());
        transaction.setDate(LocalDate.now());
        transaction.setType(type);
        transaction.setCompte(compte);
        transaction = transactionService.save(transaction);

        return transactionMapper.toResponse(transaction, compte);
    }

    /**
     * Rechercher un compte par son numéro.
     */
    public CompteDetailResponse rechercherParNumero(String numero) {
        Compte compte = compteService.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte avec le numéro '" + numero + "' introuvable !"));
        return compteMapper.toDetailResponse(compte);
    }

    /**
     * Lister les comptes par client.
     */
    public List<CompteResponse> listerComptesParClient(Long clientId) {
        clientService.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client avec l'id " + clientId + " introuvable !"));

        return compteService.findByClientId(clientId)
                .stream()
                .map(compteMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lister tous les comptes.
     */
    public List<CompteDetailResponse> listerTousLesComptes() {
        return compteService.findAll()
                .stream()
                .map(compteMapper::toDetailResponse)
                .collect(Collectors.toList());
    }

    /**
     * Consulter une transaction.
     */
    public TransactionResponse consulterTransaction(String numeroCompte, Long transactionId) {
        Compte compte = compteService.findByNumero(numeroCompte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte avec le numéro '" + numeroCompte + "' introuvable !"));

        Transaction transaction = transactionService.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction avec l'id " + transactionId + " introuvable !"));

        if (transaction.getCompte().getId() != compte.getId()) {
            throw new ResourceNotFoundException(
                    "Transaction " + transactionId + " n'appartient pas au compte '" + numeroCompte + "' !");
        }

        return transactionMapper.toResponse(transaction, compte);
    }

    private Type getOrCreateType(String libelle) {
        return typeService.findByLibelle(libelle)
                .orElseGet(() -> {
                    Type newType = new Type();
                    newType.setLibelle(libelle);
                    return typeService.save(newType);
                });
    }
}
