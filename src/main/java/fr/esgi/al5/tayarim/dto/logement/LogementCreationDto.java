package fr.esgi.al5.tayarim.dto.logement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

  @Min(value = 1, message = "error_home_invalid_owner_id")
  @NotNull(message = "error_home_invalid_owner_id")
  private Long idProprietaire;

  @Min(value = 1, message = "error_home_invalid_room_number")
  @NotNull(message = "error_home_invalid_room_number")
  private Integer nombresDeChambres;

  @Min(value = 1, message = "error_home_invalid_bed_number")
  @NotNull(message = "error_home_invalid_bed_number")
  private Integer nombresDeLits;

  @Min(value = 1, message = "error_home_invalid_bathroom_number")
  @NotNull(message = "error_home_invalid_bathroom_number")
  private Integer nombresSallesDeBains;

  @Min(value = 1, message = "error_home_invalid_max_capacity")
  @NotNull(message = "error_home_invalid_max_capacity")
  private Integer capaciteMaxPersonne;

  @Min(value = 1, message = "error_home_invalid_night_number")
  @NotNull(message = "error_home_invalid_night_number")
  private Integer nombresNuitsMin;

  @NotBlank(message = "error_home_invalid_description")
  private String description;

  @Min(value = 1, message = "error_home_invalid_price")
  @NotNull(message = "error_home_invalid_price")
  private Float prixParNuit;

  @NotBlank(message = "error_home_invalid_checkin")
  private String defaultCheckIn;

  @NotBlank(message = "error_home_invalid_checkout")
  private String defaultCheckOut;

  @Min(value = 1, message = "error_home_invalid_reservation_interval")
  @NotNull(message = "error_home_invalid_reservation_interval")
  private Integer intervalReservation;

  @NotBlank(message = "error_home_invalid_city")
  private String ville;

  @NotBlank(message = "error_home_invalid_street")
  private String rue;

  @Min(value = 1, message = "error_home_invalid_number")
  @NotNull(message = "error_home_invalid_number")
  private Integer numero;

  @Min(value = 1, message = "error_home_invalid_suffix_number")
  private String suffixeNumero;

  @NotBlank(message = "error_home_invalid_postal_code")
  private String codePostal;

  @NotBlank(message = "error_home_invalid_country")
  private String pays;

  private String etage;

  private String numeroDePorte;

  @Min(value = 1, message = "error_home_invalid_type_id")
  @NotNull(message = "error_home_invalid_type_id")
  private Long idTypeLogement;

}
