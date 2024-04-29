package fr.esgi.al5_2.Tayarim.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class AuthLoginResponseDTO {

    @NonNull
    private Long id;

    @NonNull
    private String token;

    private boolean admin;

    // No need for explicit getters, setters, or constructors
}
