package com.example.login.model; // Anger paketet som denna fil tillhör

import org.springframework.data.jpa.repository.JpaRepository; // Importerar JpaRepository-gränssnittet från Spring Data JPA
import org.springframework.stereotype.Repository; // Importerar @Repository-annoteringen

/**
 * UserRepository är ett gränssnitt för att hantera databasoperationer för användare.
 */
@Repository // Markerar gränssnittet som en repository-komponent, vilket gör att den hanteras av Spring
public interface UserRepository extends JpaRepository<User, Long> { // Utökar JpaRepository för att få standardmetoder för CRUD-operationer
    User findByEmail(String email); // Deklarerar en metod för att hitta en användare baserat på e-postadress
}