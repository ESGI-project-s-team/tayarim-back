package fr.esgi.al5.tayarim.dto.proprietaire;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la mise à jour des informations d'un administrateur. Utilisé pour passer les données
 * modifiées à l'API pour les opérations de mise à jour.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurUpdateDto {

  @Valid

  @Size(min = 1, max = 20, message = "error_admin_invalid_name")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_admin_invalid_name")
  private String nom;

  @Size(min = 1, max = 20, message = "error_admin_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_admin_invalid_firstName")
  private String prenom;

  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_admin_invalid_mail")
  private String email;

  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_admin_invalid_phone")
  private String numTel;

  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[!@#$%&*()_+\\-=\\[\\]?]).{8,}$",
      message = "error_admin_invalid_password")
  private String motDePasse;

  // No need for explicit getters, setters, or constructors
}
