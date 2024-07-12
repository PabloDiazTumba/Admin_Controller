package com.example.login.model; // Anger paketet som denna fil tillhör

import jakarta.persistence.*; // Importerar JPA-annoteringar för databasinteraktion
import lombok.Getter; // Importerar Lombok-annoteringen @Getter för att generera getter-metoder
import lombok.Setter; // Importerar Lombok-annoteringen @Setter för att generera setter-metoder

import java.io.Serializable; // Importerar Serializable-gränssnittet

/**
 * User-klassen representerar en användare i databasen.
 */
@Setter // Genererar automatiskt setter-metoder för alla fält
@Getter // Genererar automatiskt getter-metoder för alla fält
@Entity // Markerar klassen som en JPA-entitet
@Table(name = "users") // Anger namnet på tabellen som denna entitet är kopplad till
public class User implements Serializable { // Implementerar Serializable-gränssnittet för att möjliggöra serialisering

    @Id // Markerar fältet som den primära nyckeln
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anger att värdet ska genereras automatiskt av databasen
    private Long id; // Deklarerar fältet id som den primära nyckeln

    @Column(nullable = false, unique = true) // Anger att kolumnen inte får vara null och måste vara unik
    private String email; // Deklarerar fältet email

    @Column(nullable = false) // Anger att kolumnen inte får vara null
    private String password; // Deklarerar fältet password

    @Column(nullable = false) // Anger att kolumnen inte får vara null
    private String role; // Deklarerar fältet role
}