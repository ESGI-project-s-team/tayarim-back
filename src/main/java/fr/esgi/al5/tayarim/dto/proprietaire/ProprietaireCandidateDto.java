package fr.esgi.al5.tayarim.dto.proprietaire;

import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO pour la candidature de nouveaux propriétaires dans le système. Contient des champs avec des
 * validations pour assurer l'intégrité des données lors de l'enregistrement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireCandidateDto {

  @Valid

  @NotBlank(message = "error_owner_invalid_name")
  @Size(min = 1, max = 20, message = "error_owner_invalid_name")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_owner_invalid_name")
  private String nom;

  @NotBlank(message = "error_owner_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_owner_invalid_firstName")
  @Size(min = 1, max = 20, message = "error_owner_invalid_firstName")
  private String prenom;

  @NotBlank(message = "error_owner_invalid_mail")
  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_owner_invalid_mail")
  //@Email(message = "error_owner_invalid_mail")
  private String email;

  @NotBlank(message = "error_owner_invalid_phone")
  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_owner_invalid_phone")
  private String numTel;

  @NotBlank(message = "error_owner_invalid_address")
  @Size(min = 1, max = 300, message = "error_owner_invalid_address")
  private String adresse;

  @NotNull(message = "error_candidate_invalid_isLouable")
  private Boolean isLouable;

  @NotNull(message = "error_candidate_invalid_title")
  @Size(min = 1, max = 100, message = "error_candidate_invalid_title")
  private String titre;

  @Min(value = 1, message = "error_candidate_invalid_bedrooms")
  private Integer nombresDeChambres;

  @Min(value = 1, message = "error_candidate_invalid_beds")
  private Integer nombresDeLits;

  @Min(value = 1, message = "error_candidate_invalid_bathrooms")
  private Integer nombresSallesDeBains;

  @Min(value = 1, message = "error_candidate_invalid_maxCapacity")
  private Integer capaciteMaxPersonne;

  @NotBlank(message = "error_candidate_invalid_description")
  @Size(min = 1, max = 300, message = "error_candidate_invalid_description")
  private String description;

  @NotBlank(message = "error_candidate_invalid_ville")
  @Size(min = 1, max = 30, message = "error_candidate_invalid_ville")
  private String ville;

  @NotBlank(message = "error_candidate_invalid_addressLogement")
  @Size(min = 1, max = 300, message = "error_candidate_invalid_addressLogement")
  private String addressLogement;

  @NotBlank(message = "error_candidate_invalid_codePostal")
  @Size(min = 1, max = 10, message = "error_candidate_invalid_codePostal")
  private String codePostal;

  @NotBlank(message = "error_candidate_invalid_pays")
  @Size(min = 1, max = 30, message = "error_candidate_invalid_pays")
  private String pays;

  @Size(min = 1, max = 10, message = "error_candidate_invalid_etage")
  private String etage;

  @Size(min = 1, max = 10, message = "error_candidate_invalid_numeroDePorte")
  private String numeroDePorte;

  @Size(min = 1, max = 10, message = "error_candidate_invalid_rules")
  private List<Long> reglesLogement;

  @Size(min = 1, max = 10, message = "error_candidate_invalid_amenagements")
  private List<Long> amenagements;

  @Size(min = 1, max = 10, message = "error_candidate_invalid_files")
  private List<MultipartFile> files;

  @NotNull(message = "error_candidate_invalid_idTypeLogement")
  @Min(value = 1, message = "error_candidate_invalid_idTypeLogement")
  private Long idTypeLogement;

  // No need for explicit getters, setters, or constructors
}
