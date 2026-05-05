package sn.l3gl.banque.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
                @JsonSubTypes.Type(value = CreateCompteExistingRequest.class, name = "EXISTING"),
                @JsonSubTypes.Type(value = CreateCompteNewRequest.class, name = "NEW")
})
@Schema(
                description = "Requête de création de compte",
                subTypes = { CreateCompteExistingRequest.class, CreateCompteNewRequest.class },
                discriminatorProperty = "type"
)
public abstract class CreateCompteRequest {
        @Schema(description = "Type de création : EXISTING ou NEW", example = "NEW")
        private String type;

        @Min(value = 50000, message = "Le dépôt initial doit être d'au moins 50000 !")
        private int solde;

        @NotNull(message = "date de creation obligatoire !")
        private LocalDate dateCreation;
}
