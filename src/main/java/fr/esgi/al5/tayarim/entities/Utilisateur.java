package fr.esgi.al5.tayarim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Classe abstraite de base pour les utilisateurs dans le système. Cette classe définit les
 * propriétés communes à tous les types d'utilisateurs, telles que nom, prénom, email, numéro de
 * téléphone et mot de passe. Elle utilise la stratégie de persistance 'JOINED' pour permettre
 * l'héritage des entités Utilisateur dans la base de données.
 */
@Data
@Entity(name = "UTILISATEUR")
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