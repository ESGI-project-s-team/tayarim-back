package fr.esgi.al5.tayarim.dto.logement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * DTO pour la création de nouveaux logement dans le système. Contient des champs avec des
 * validations pour assurer l'intégrité des données lors de l'enregistrement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@NoArgsConstructor
@Builder
public class LogementCreationDto {

  @Valid

  @NotBlank
  private String titre;

  @Min(value = 1)
  @NotNull
  private Long idProprietaire;

  @Min(value = 1)
  @NotNull
  private Integer nombresDeChambres;

  @Min(value = 1)
  @NotNull
  private Integer nombresDeLits;

  @Min(value = 1)
  @NotNull
  private Integer nombresSallesDeBains;

  @Min(value = 1)
  @NotNull
  private Integer capaciteMaxPersonne;

  @Min(value = 1)
  @NotNull
  private Integer nombresNuitsMin;

  @NotBlank
  private String description;

  @Min(value = 1)
  @NotNull
  private Float prixParNuit;

  @NotBlank
  private String defaultCheckIn;

  @NotBlank
  private String defaultCheckOut;

  @Min(value = 1)
  @NotNull
  private Integer intervalReservation;

  @NotBlank
  private String ville;

  @NotBlank
  private String rue;

  @Min(value = 1)
  @NotNull
  private Integer numero;

  @Min(value = 1)
  private String suffixeNumero;

  @NotBlank
  @NotNull
  private String codePostal;

  @NotBlank
  private String pays;

  private String etage;

  private String numeroDePorte;

  @Min(value = 1)
  @NotNull
  private Long idTypeLogement;

}
