package sn.l3gl.banque.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.l3gl.banque.api.dto.*;
import sn.l3gl.banque.api.exception.ResourceNotFoundException;
import sn.l3gl.banque.api.helper.CompteHelper;
import sn.l3gl.banque.api.mapper.CompteMapper;
import sn.l3gl.banque.api.mapper.TransactionMapper;
import sn.l3gl.banque.api.model.Compte;
import sn.l3gl.banque.api.model.Transaction;
import sn.l3gl.banque.api.repository.ClientRepository;
import sn.l3gl.banque.api.repository.CompteRepository;
import sn.l3gl.banque.api.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteService {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final CompteMapper compteMapper;
    private final TransactionMapper transactionMapper;
    private final CompteHelper compteHelper;

    /**
     * Créer un compte bancaire (délègue au helper)
     */
    @Transactional
    public CompteDetailResponse creerCompte(CreateCompteRequest request) {
        return compteHelper.saveCompte(request);
    }

    /**
     * Rechercher un compte par son numéro (retourne infos compte + client)
     */
    public CompteDetailResponse rechercherParNumero(String numero) {
        Compte compte = compteRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte avec le numéro '" + numero + "' introuvable !"));
        return compteMapper.toDetailResponse(compte);
    }

    /**
     * Lister les comptes d'un client (retourne seulement les infos des comptes)
     */
    public List<CompteResponse> listerComptesParClient(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client avec l'id " + clientId + " introuvable !"));

        return compteRepository.findByClientId(clientId)
                .stream()
                .map(compteMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lister tous les comptes
     */
    public List<CompteDetailResponse> listerTousLesComptes() {
        return compteRepository.findAll()
                .stream()
                .map(compteMapper::toDetailResponse)
                .collect(Collectors.toList());
    }

    /**
     * Effectuer une transaction (délègue au helper)
     */
    @Transactional
    public TransactionResponse effectuerTransaction(String numeroCompte, TransactionRequest request) {
        return compteHelper.handleTransaction(numeroCompte, request);
    }

    /**
     * Consulter une transaction par son ID
     */
    public TransactionResponse consulterTransaction(String numeroCompte, Long transactionId) {
        Compte compte = compteRepository.findByNumero(numeroCompte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte avec le numéro '" + numeroCompte + "' introuvable !"));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction avec l'id " + transactionId + " introuvable !"));

        if (transaction.getCompte().getId() != compte.getId()) {
            throw new ResourceNotFoundException(
                    "Transaction " + transactionId + " n'appartient pas au compte '" + numeroCompte + "' !");
        }

        return transactionMapper.toResponse(transaction, compte);
    }
}
