package fr.esgi.al5_2.Tayarim;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;

@SpringBootApplication
public class TayarimApplication {

  public static void main(String[] args) {
    SpringApplication.run(TayarimApplication.class, args);
  }

}
