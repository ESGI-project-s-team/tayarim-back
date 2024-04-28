package fr.esgi.al5_2.Tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPROPRIETAIRE", nullable = false)
    @NonNull
    private Proprietaire proprietaire;

    // public Logement(@NonNull Proprietaire proprietaire){
    //     this.proprietaire = proprietaire;
    // }

}
