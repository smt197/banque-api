package sn.l3gl.banque.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.l3gl.banque.api.dto.*;
import sn.l3gl.banque.api.service.CompteService;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/comptes")
@RestController
@RequiredArgsConstructor
public class CompteController {

    private final CompteService compteService;

    /**
     * Créer un nouveau compte bancaire
     * POST /api/comptes
     */
    @PostMapping
    public ResponseEntity<CompteDetailResponse> creerCompte(@Valid @RequestBody CreateCompteRequest request) {
        CompteDetailResponse response = compteService.creerCompte(request);
        URI uri = URI.create("/api/comptes/" + response.getNumero());
        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Rechercher un compte par son numéro (retourne infos compte + client)
     * GET /api/comptes/{numero}
     */
    @GetMapping("/{numero}")
    public ResponseEntity<CompteDetailResponse> rechercherParNumero(@PathVariable String numero) {
        CompteDetailResponse response = compteService.rechercherParNumero(numero);
        return ResponseEntity.ok(response);
    }

    /**
     * Lister tous les comptes
     * GET /api/comptes
     */
    @GetMapping
    public ResponseEntity<List<CompteDetailResponse>> listerTousLesComptes() {
        List<CompteDetailResponse> comptes = compteService.listerTousLesComptes();
        return ResponseEntity.ok(comptes);
    }

    /**
     * Lister les comptes d'un client (retourne seulement les infos des comptes)
     * GET /api/comptes/client/{clientId}
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CompteResponse>> listerComptesParClient(@PathVariable Long clientId) {
        List<CompteResponse> comptes = compteService.listerComptesParClient(clientId);
        return ResponseEntity.ok(comptes);
    }

    /**
     * Effectuer une transaction (dépôt ou retrait) sur un compte
     * POST /api/comptes/{numero}/transactions
     * Retourne 201 Created avec l'URL de la transaction
     */
    @PostMapping("/{numero}/transactions")
    public ResponseEntity<TransactionResponse> effectuerTransaction(
            @PathVariable String numero,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = compteService.effectuerTransaction(numero, request);
        URI uri = URI.create("/api/comptes/" + numero + "/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Consulter une transaction par son ID
     * GET /api/comptes/{numero}/transactions/{transactionId}
     */
    @GetMapping("/{numero}/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> consulterTransaction(
            @PathVariable String numero,
            @PathVariable Long transactionId) {
        TransactionResponse response = compteService.consulterTransaction(numero, transactionId);
        return ResponseEntity.ok(response);
    }
}
