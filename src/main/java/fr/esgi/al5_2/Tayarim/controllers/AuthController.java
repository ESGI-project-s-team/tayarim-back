package fr.esgi.al5_2.Tayarim.controllers;

import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginDTO;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authProprietaire) {
        this.authService = authProprietaire;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDTO> login(@Valid @RequestBody AuthLoginDTO authLoginDTO){
        return new ResponseEntity<>(
                authService.login(authLoginDTO.getEmail(), authLoginDTO.getMotDePasse()),
                HttpStatus.OK
        );
    }

    @GetMapping("")
    public ResponseEntity<AuthLoginResponseDTO> auth(@RequestHeader("Authorization") String authHeader){

        String jwtToken = getTokenFromHeader(authHeader);

        return new ResponseEntity<>(
                authService.auth(jwtToken),
                HttpStatus.OK
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader){

        String jwtToken = getTokenFromHeader(authHeader);
        authService.logout(jwtToken);

        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    private String getTokenFromHeader(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenExpireOrInvalidException();
        }

        return authHeader.substring(7);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        ArrayList<String> errors = new ArrayList<>();
        Map<String, List<String>> errorMapping = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        errorMapping.put("errors", errors);

        return errorMapping;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UtilisateurNotFoundException.class})
    public Map<String, List<String>> utilisateurNotFoundException(UtilisateurNotFoundException ex){
        return mapException(ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpireOrInvalidException.class})
    public Map<String, List<String>> tokenExpireOrInvalidException(TokenExpireOrInvalidException ex){
        return mapException(ex);
    }

    private Map<String, List<String>> mapException(RuntimeException exception){
        ArrayList<String> errors = new ArrayList<>();
        Map<String, List<String>> errorMapping = new HashMap<>();
        errors.add(exception.getMessage());

        errorMapping.put("errors", errors);

        return errorMapping;
    }
}
