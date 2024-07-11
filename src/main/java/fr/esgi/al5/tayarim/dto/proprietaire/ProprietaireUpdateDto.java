package fr.esgi.al5.tayarim.dto.proprietaire;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la mise à jour des informations d'un propriétaire. Permet de modifier les détails d'un
 * propriétaire existant dans la base de données.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireUpdateDto {

  @Valid

  @Size(min = 1, max = 20, message = "error_owner_invalid_name")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_owner_invalid_name")
  private String nom;

  @Size(min = 1, max = 20, message = "error_owner_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_owner_invalid_firstName")
  private String prenom;

  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_owner_invalid_mail")
  private String email;

  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_owner_invalid_phone")
  private String numTel;

  @Size(min = 1, max = 300, message = "error_owner_invalid_address")
  private String adresse;

  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[!@#$%&*()_+\\-=\\[\\]?]).{8,}$",
      message = "error_owner_invalid_password")
  private String motDePasse;

  @DecimalMin(value = "0.0", message = "error_owner_invalid_commission")
  @DecimalMax(value = "99.99", message = "error_owner_invalid_commission")
  private Float commission;

  @Pattern(regexp = "fr|en", message = "error_owner_invalid_lang")
  @Size(min = 2, max = 2, message = "error_owner_invalid_lang")
  private String lang;
  // No need for explicit getters, setters, or constructors
}
