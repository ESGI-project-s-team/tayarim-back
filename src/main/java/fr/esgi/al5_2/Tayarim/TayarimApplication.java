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

  // @Bean
  // public CommandLineRunner demo(ProprietaireRepository repository) {
  //     return (args) -> {

  // 		// Récupérer tous les propriétaires et les afficher
  //         List<Proprietaire> proprietaires = repository.findAll();
  // 		System.out.println(proprietaires.size());
  //         for (Proprietaire p : proprietaires) {
  //             System.out.println(p.getId() + " " + p.getNom() + " " + p.getPrenom());
  //         }

  //         // Exemple: Enregistrer une nouvelle personne
  //         Proprietaire proprietaire = new Proprietaire("Ferreira", "Mathieu", "test@gmail.com", "0675000495", "password", LocalDateTime.now());
  //         repository.save(proprietaire);

  // 		// Exemple: Enregistrer une nouvelle personne
  //         Proprietaire proprietaire2 = new Proprietaire("Ferreira", "Mat", "test@gmail.com", "0675000495", "password", LocalDateTime.now());
  //         repository.save(proprietaire2);

  // 		// Récupérer tous les propriétaires et les afficher
  //         proprietaires = repository.findAll();
  // 		System.out.println(proprietaires.size());
  //         for (Proprietaire p : proprietaires) {
  //             System.out.println(p.getId() + " " + p.getNom() + " " + p.getPrenom());
  //         }
  //     };
  // }

}

// @Bean
// public CommandLineRunner showTables(DataSource dataSource) {
//     return args -> {
//         try (Connection conn = dataSource.getConnection()) {
//             // Récupère les métadonnées de la base de données
//             var metadata = conn.getMetaData();
//             // Récupère la liste des tables
//             try (ResultSet rs = metadata.getTables(null, null, "%", new String[] { "TABLE" })) {
//                 System.out.println("Liste des tables :");
//                 while (rs.next()) {
//                     // Affiche le nom de chaque table
//                     System.out.println("- " + rs.getString("TABLE_NAME"));
//                 }
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     };
//}

// @Bean
// public CommandLineRunner showTableSchemas(DataSource dataSource) {
// 	return args -> {
// 		try (Connection conn = dataSource.getConnection()) {
// 			DatabaseMetaData metadata = conn.getMetaData();

// 			// Récupère la liste des tables
// 			try (ResultSet tables = metadata.getTables(null, null, "%", new String[]{"TABLE"})) {
// 				while (tables.next()) {
// 					String tableName = tables.getString("TABLE_NAME");
// 					System.out.println("Table: " + tableName);

// 					// Récupérer les détails des colonnes pour chaque table
// 					try (ResultSet columns = metadata.getColumns(null, null, tableName, "%")) {
// 						while (columns.next()) {
// 							String columnName = columns.getString("COLUMN_NAME");
// 							String columnType = columns.getString("TYPE_NAME");
// 							int columnSize = columns.getInt("COLUMN_SIZE");

// 							System.out.println("\tColumn Name: " + columnName);
// 							System.out.println("\t\tType: " + columnType);
// 							System.out.println("\t\tSize: " + columnSize);
// 						}
// 					}
// 					System.out.println(); // Saut de ligne entre les tables pour une meilleure lisibilité
// 				}
// 			}
// 		} catch (SQLException e) {
// 			e.printStackTrace();
// 		}
// 	};

// }