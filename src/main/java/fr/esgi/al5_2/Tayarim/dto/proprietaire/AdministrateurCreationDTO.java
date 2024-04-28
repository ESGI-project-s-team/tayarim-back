package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurCreationDTO {

    @Valid

    @NotBlank(message = "error_admin_invalid_name")
    private String nom;

    @NotBlank(message = "error_admin_invalid_firstName")
    private String prenom;
    
    @NotBlank(message = "error_admin_invalid_mail")
    @Email(message = "error_adminr_invalid_mail")
    private String email;
    
    @NotBlank(message = "error_admin_invalid_phone")
    @Pattern(regexp = "\\+?[\\d\\s\\-.()]+", message = "error_admin_invalid_phone")
    private String numTel;
    
    @NotBlank(message = "error_admin_invalid_password")
    private String motDePasse;

    // No need for explicit getters, setters, or constructors
}
