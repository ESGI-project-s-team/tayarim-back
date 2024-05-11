package fr.esgi.al5.tayarim.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor// Generates an all-args constructor
public class AuthRefreshResponseDto {

    @NonNull
    private String accessToken;

    @NonNull
    private String refreshToken;

    @NonNull
    private String tokenType;

    // No need for explicit getters, setters, or constructors
}
