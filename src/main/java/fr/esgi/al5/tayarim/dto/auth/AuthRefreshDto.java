package fr.esgi.al5.tayarim.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRefreshDto {

    @Valid

    @NotBlank(message = "error_jwt_invalid_refreshToken") //TODO add to dico
    private String refreshToken;
}
