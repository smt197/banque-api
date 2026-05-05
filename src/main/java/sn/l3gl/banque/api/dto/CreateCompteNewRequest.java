package sn.l3gl.banque.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "CreateCompteNewRequest",
        description = "Création de compte pour un nouveau client",
        example = "{\"type\": \"NEW\", \"solde\": 50000, \"dateCreation\": \"2026-05-05\", \"prenom\": \"Jean\", \"nom\": \"Dupont\", \"telephone\": \"771234567\", \"adresse\": \"Dakar\", \"numPiece\": \"123456789\", \"dateNaissance\": \"1990-01-01\"}"
)
public class CreateCompteNewRequest extends CreateCompteRequest {

    @NotBlank(message = "Le prénom est obligatoire !")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire !")
    private String nom;

    @NotBlank(message = "Le téléphone est obligatoire !")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire !")
    private String adresse;

    @NotBlank(message = "Le numéro de pièce est obligatoire !")
    private String numPiece;

    @NotNull(message = "La date de naissance est obligatoire !")
    private LocalDate dateNaissance;
}
