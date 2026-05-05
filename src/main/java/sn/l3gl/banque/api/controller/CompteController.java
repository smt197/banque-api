package sn.l3gl.banque.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.l3gl.banque.api.dto.*;
import sn.l3gl.banque.api.helper.CompteHelper;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/comptes")
@RestController
@RequiredArgsConstructor
public class CompteController {

    private final CompteHelper compteHelper;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CompteDetailResponse> creerCompte(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails de la création du compte (Client existant ou nouveau)",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(oneOf = {
                                    CreateCompteExistingRequest.class, CreateCompteNewRequest.class }),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Nouveau Client", summary = "Création avec un nouveau client", value = "{\"type\": \"NEW\", \"solde\": 50000, \"dateCreation\": \"2026-05-05\", \"prenom\": \"Jean\", \"nom\": \"Dupont\", \"telephone\": \"771234567\", \"adresse\": \"Dakar\", \"numPiece\": \"123456789\", \"dateNaissance\": \"1990-01-01\"}"),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Client Existant", summary = "Création avec un client existant", value = "{\"type\": \"EXISTING\", \"solde\": 50000, \"dateCreation\": \"2026-05-05\", \"clientId\": 1}")
                            }
                    )
            )
            @Valid @RequestBody CreateCompteRequest request) {
        CompteDetailResponse response = compteHelper.saveCompte(request);
        URI uri = URI.create("/api/comptes/" + response.getNumero());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CompteDetailResponse> rechercherParNumero(@PathVariable String numero) {
        CompteDetailResponse response = compteHelper.rechercherParNumero(numero);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CompteDetailResponse>> listerTousLesComptes() {
        List<CompteDetailResponse> comptes = compteHelper.listerTousLesComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CompteResponse>> listerComptesParClient(@PathVariable Long clientId) {
        List<CompteResponse> comptes = compteHelper.listerComptesParClient(clientId);
        return ResponseEntity.ok(comptes);
    }

    @PostMapping("/{numero}/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionResponse> effectuerTransaction(
            @PathVariable String numero,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = compteHelper.handleTransaction(numero, request);
        URI uri = URI.create("/api/comptes/" + numero + "/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{numero}/transactions/{transactionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionResponse> consulterTransaction(
            @PathVariable String numero,
            @PathVariable Long transactionId) {
        TransactionResponse response = compteHelper.consulterTransaction(numero, transactionId);
        return ResponseEntity.ok(response);
    }
}
