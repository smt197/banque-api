package sn.l3gl.banque.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateCompteRequest {
    @NotBlank(message = "Numero de compte obligatoire !")
    private String numCompte;
    @Min(value = 50000, message = "Le dépôt initial doit être d'au moins 50000 !")
    private int solde;
    @NotNull(message = "date de creation obligatoire !")
    private LocalDate dateCreation;

    private long clientId;
    private String prenom;
    private String nom;
    private String telephone;
    private String adresse;
    private String numPiece;
    private LocalDate dateNaissance;

}
