package fr.esgi.al5.tayarim.dto.logement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO pour la création de nouveaux logement dans le système. Contient des champs avec des
 * validations pour assurer l'intégrité des données lors de l'enregistrement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class LogementUpdateDto {

  @Valid

  @Size(min = 1, max = 100, message = "error_home_invalid_title")
  private String titre;

  private Boolean isLouable;

  @Min(value = 1, message = "error_home_invalid_owner_id")
  private Long idProprietaire;

  @Min(value = 1, message = "error_home_invalid_room_number")
  private Integer nombresDeChambres;

  @Min(value = 1, message = "error_home_invalid_bed_number")
  private Integer nombresDeLits;

  @Min(value = 1, message = "error_home_invalid_bathroom_number")
  private Integer nombresSallesDeBains;

  @Min(value = 1, message = "error_home_invalid_max_capacity")
  private Integer capaciteMaxPersonne;

  @Min(value = 1, message = "error_home_invalid_night_number")
  private Integer nombresNuitsMin;

  @Size(min = 1, max = 500, message = "error_home_invalid_description")
  private String description;

  @Min(value = 1, message = "error_home_invalid_price")
  private Float prixParNuit;

  @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "error_home_invalid_checkin")
  private String defaultCheckIn;

  @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "error_home_invalid_checkout")
  private String defaultCheckOut;

  @Min(value = 1, message = "error_home_invalid_reservation_interval")
  private Integer intervalReservation;

  @Size(min = 1, max = 30, message = "error_home_invalid_city")
  private String ville;

  @Size(min = 1, max = 150, message = "error_home_invalid_address")
  private String adresse;

  @Size(min = 1, max = 10, message = "error_home_invalid_postal_code")
  private String codePostal;

  @Size(min = 1, max = 30, message = "error_home_invalid_country")
  private String pays;

  @Size(min = 1, max = 30, message = "error_home_invalid_etage")
  private String etage;

  @Size(min = 1, max = 30, message = "error_home_invalid_numero_de_porte")
  private String numeroDePorte;

  @Min(value = 1, message = "error_home_invalid_type_id")
  private Long idTypeLogement;

  @Size(min = 1, max = 1000, message = "error_home_invalid_rules")
  private List<Long> reglesLogement;

  @Size(min = 1, max = 1000, message = "error_home_invalid_facilities")
  private List<Long> amenagements;

  @Size(min = 1, max = 1000, message = "error_home_invalid_photos_list_length")
  private List<MultipartFile> files;

  @Size(min = 1, max = 1000, message = "error_home_invalid_current_photos_list_length")
  private List<Long> currentImages;
}
