package fr.esgi.al5.tayarim.dto.facture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe pour la gestion des Dto de facture.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class FactureDto {

  @NonNull
  private Long id;

  @NonNull
  private String numeroFacture;

  @NonNull
  private String dateFacture;

  @NonNull
  private Float montant;

  @NonNull
  private Long idProprietaire;

  @NonNull
  private String url;

  @NonNull
  private Boolean isSend;

  private String nomProprietaire;

  private String prenomProprietaire;


}
