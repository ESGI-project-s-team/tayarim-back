package fr.esgi.al5_2.Tayarim.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthDTO {

    @Valid

    @NotNull(message = "error_auth_missing_id") //TODO add to dico
    private Long id;


}