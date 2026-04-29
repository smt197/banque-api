package sn.l3gl.banque.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    @Min(value = 1, message = "Le montant doit etre positif !")
    private long montant;

    @NotBlank(message = "Le type de transaction est obligatoire (DEPOT ou RETRAIT) !")
    private String typeTransaction;
}
