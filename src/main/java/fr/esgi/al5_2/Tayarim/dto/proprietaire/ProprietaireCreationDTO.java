package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class ProprietaireCreationDTO {

    @Valid

    @NotBlank(message = "error_owner_invalid_name")
    private String nom;

    @NotBlank(message = "error_owner_invalid_firstName")
    private String prenom;
    
    @NotBlank(message = "error_owner_invalid_mail")
    @Email(message = "error_owner_invalid_mail")
    private String email;
    
    @NotBlank(message = "error_owner_invalid_phone")
    private String numTel;
    
    @NotBlank(message = "error_owner_invalid_password")
    private String motDePasse;

    // No need for explicit getters, setters, or constructors
}
