package fr.esgi.al5_2.Tayarim.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPROPRIETAIRE")
    private Proprietaire proprietaire;

    // public Logement(@NonNull Proprietaire proprietaire){
    //     this.proprietaire = proprietaire;
    // }

}
