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
public class ClientResponse {
    private Long id;
    private String numPiece;
    private String prenom;
    private String nom;
    private String adresse;
    private LocalDate dateNaissance;
    private String telephone;
}
