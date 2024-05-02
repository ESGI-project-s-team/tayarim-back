package fr.esgi.al5_2.Tayarim.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe abstraite de base pour les utilisateurs dans le système. Cette classe définit les
 * propriétés communes à tous les types d'utilisateurs, telles que nom, prénom, email, numéro de
 * téléphone et mot de passe. Elle utilise la stratégie de persistance 'JOINED' pour permettre
 * l'héritage des entités Utilisateur dans la base de données.
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public abstract class Utilisateur {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NOM", nullable = false)
  @NonNull
  private String nom;

  @Column(name = "PRENOM", nullable = false)
  @NonNull
  private String prenom;

  @Column(name = "EMAIL", nullable = false)
  @NonNull
  private String email;

  @Column(name = "NUMTEL", nullable = false)
  @NonNull
  private String numTel;

  @Column(name = "MOTDEPASSE", nullable = false)
  @NonNull
  private String motDePasse;
}