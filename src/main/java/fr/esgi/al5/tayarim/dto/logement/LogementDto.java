package fr.esgi.al5.tayarim.dto.logement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) représentant un logement dans le système. Ce DTO est utilisé
 * pour encapsuler les informations essentielles d'un logement, y compris son identifiant et
 * l'identifiant de son propriétaire, facilitant ainsi les opérations de gestion des logements.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class LogementDto {

  @NonNull
  private Long id;
  @NonNull
  private String titre;
  @NonNull
  private Long idProprietaire;
  @NonNull
  private Boolean isLouable;
  @NonNull
  private Integer nombresDeChambres;
  @NonNull
  private Integer nombresDeLits;
  @NonNull
  private Integer nombresSallesDeBains;
  @NonNull
  private Integer capaciteMaxPersonne;
  @NonNull
  private Integer nombresNuitsMin;
  @NonNull
  private String description;
  @NonNull
  private Float note;
  @NonNull
  private Float prixParNuit;
  @NonNull
  private String defaultCheckIn;
  @NonNull
  private String defaultCheckOut;
  @NonNull
  private String adresseComplete;
  @NonNull
  private String adresse;
  @NonNull
  private String ville;
  @NonNull
  private String codePostal;
  @NonNull
  private String pays;

  private String etage;

  private String numeroDePorte;

  @NonNull
  private String typeLogement;


}
