package fr.esgi.al5.tayarim.dto.depense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) représentant une Depense. Ce DTO est utilisé
 * pour encapsuler les informations essentielles d'une Depense.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor
public class DepenseDto {

  @NonNull
  private Long id;

  @NonNull
  private String libelle;

  @NonNull
  private Float prix;

  @NonNull
  private String date;

  @NonNull
  private Long idLogement;

  @NonNull
  private String titreLogement;
}
