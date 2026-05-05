package sn.l3gl.banque.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "CreateCompteExistingRequest",
        description = "Création de compte pour un client existant",
        example = "{\"type\": \"EXISTING\", \"solde\": 50000, \"dateCreation\": \"2026-05-05\", \"clientId\": 1}"
)
public class CreateCompteExistingRequest extends CreateCompteRequest {
    
    @Min(value = 1, message = "L'ID du client doit être valide !")
    @Schema(description = "ID du client existant", example = "1")
    private long clientId;
}
