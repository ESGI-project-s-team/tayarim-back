package fr.esgi.al5_2.Tayarim.models;

import java.util.ArrayList;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Proprietaire extends Utilisateur{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proprietaire")
    private ArrayList<Logement> logements;

    @Column(name = "dateInscription", nullable = false)
    private Date dateInscription;
    

}
