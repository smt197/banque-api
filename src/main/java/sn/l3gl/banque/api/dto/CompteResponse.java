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
public class CompteResponse {
    private long id;
    private String numero;
    private long solde;
    private LocalDate dateCreation;
}
