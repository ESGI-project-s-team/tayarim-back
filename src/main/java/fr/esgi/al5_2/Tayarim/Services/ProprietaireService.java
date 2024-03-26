package fr.esgi.al5_2.Tayarim.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;

@Service
public class ProprietaireService {
    @Autowired
    private ProprietaireRepository proprietaireRepository;
}
