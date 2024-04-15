package fr.esgi.al5_2.Tayarim.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor //car 1 attribut ? (sinon sa plante mais que pour cette classe)
public class AuthDTO {

    @Valid

    @NotNull(message = "error_auth_missing_id") //TODO add to dico
    private Long id;

}