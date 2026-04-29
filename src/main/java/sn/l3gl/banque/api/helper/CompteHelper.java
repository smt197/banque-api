package sn.l3gl.banque.api.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
import sn.l3gl.banque.api.repository.ClientRepository;
import sn.l3gl.banque.api.repository.CompteRepository;
import sn.l3gl.banque.api.repository.TransactionRepository;
import sn.l3gl.banque.api.repository.TypeRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final TypeRepository typeRepository;
    private final CompteMapper compteMapper;
    private final ClientMapper clientMapper;
    private final TransactionMapper transactionMapper;

    /**
     * Orchestrer la création d'un compte bancaire.
     * Si clientId != 0, on cherche le client existant, sinon on crée un nouveau client.
     * On sauvegarde le compte et on enregistre la transaction initiale.
     */
    public CompteDetailResponse saveCompte(CreateCompteRequest request) {
        Client client;

        // Si id != null on va chercher le Client, s'il n'existe pas on leve une exception
        if (request.getClientId() > 0) {
            client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client avec l'id " + request.getClientId() + " introuvable !"));
        } else {
            // Mapper createCompteRequest en nouveau Client
            client = clientMapper.toEntity(request);
            client = clientRepository.save(client);
        }

        // Mapper createCompteRequest en Compte
        Compte compte = compteMapper.toEntity(request, client);
        
        // Générer le numéro de compte automatiquement si nécessaire
        // Format: CE- + 8 chiffres aléatoires
        String generatedNumero = "CE-" + (int)(Math.random() * 90000000 + 10000000);
        compte.setNumero(generatedNumero);

        // Sauvegarder le compte
        compte = compteRepository.save(compte);

        // Enregistrer la transaction initiale (dépôt)
        Type depotType = getOrCreateType("DEPOT");
        Transaction transaction = new Transaction();
        transaction.setMontant(request.getSolde());
        transaction.setDate(request.getDateCreation());
        transaction.setType(depotType);
        transaction.setCompte(compte);
        transactionRepository.save(transaction);

        // Retourner les infos du compte créé
        return compteMapper.toDetailResponse(compte);
    }

    /**
     * Orchestrer une transaction (dépôt ou retrait).
     */
    public TransactionResponse handleTransaction(String numeroCompte, TransactionRequest request) {
        Compte compte = compteRepository.findByNumero(numeroCompte)
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

        // Sauvegarder le nouveau solde
        compteRepository.save(compte);

        // Enregistrer la transaction
        Type type = getOrCreateType(typeStr);
        Transaction transaction = new Transaction();
        transaction.setMontant(request.getMontant());
        transaction.setDate(LocalDate.now());
        transaction.setType(type);
        transaction.setCompte(compte);
        transaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(transaction, compte);
    }

    /**
     * Récupérer ou créer un type de transaction (DEPOT / RETRAIT)
     */
    private Type getOrCreateType(String libelle) {
        return typeRepository.findByLibelle(libelle)
                .orElseGet(() -> {
                    Type newType = new Type();
                    newType.setLibelle(libelle);
                    return typeRepository.save(newType);
                });
    }
}
