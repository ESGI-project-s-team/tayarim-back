package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProprietaireLoginDTO {

    @Valid

    @NotBlank(message = "error_owner_invalid_mail")
    @Email(message = "error_owner_invalid_mail")
    private String email;

    @NotBlank(message = "error_owner_invalid_password")
    private String motDePasse;
}
