package fr.esgi.al5_2.Tayarim.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esgi.al5_2.Tayarim.Services.ProprietaireService;

@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController {

    @Autowired
    private ProprietaireService proprietaireService;
}
