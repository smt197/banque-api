package sn.l3gl.banque.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private long id;
    private long montant;
    private LocalDate date;
    private String type;
    private String numeroCompte;
    private long nouveauSolde;
}
