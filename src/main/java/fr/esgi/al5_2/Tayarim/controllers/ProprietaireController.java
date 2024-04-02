package fr.esgi.al5_2.Tayarim.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.esgi.al5_2.Tayarim.Services.ProprietaireService;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import lombok.NonNull;

@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController {

    @Autowired
    private ProprietaireService proprietaireService;

    @PostMapping("")
    public ResponseEntity<ProprietaireDTO> creeProprietaire(@NonNull @RequestBody ProprietaireCreationDTO proprietaireCreationDTO/* , @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement*/) {
        return new ResponseEntity<>(
            proprietaireService.creeProprietaire(proprietaireCreationDTO, false), 
            HttpStatus.CREATED);
    }
}
