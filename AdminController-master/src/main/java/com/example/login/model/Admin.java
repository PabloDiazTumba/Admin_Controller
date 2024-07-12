package com.example.login.model; // Anger paketet som denna fil tillhör

import jakarta.annotation.PostConstruct; // Importerar PostConstruct-annoteringen
import org.springframework.beans.factory.annotation.Autowired; // Importerar @Autowired-annoteringen
import org.springframework.security.crypto.password.PasswordEncoder; // Importerar PasswordEncoder-gränssnittet
import org.springframework.stereotype.Service; // Importerar @Service-annoteringen

/**
 * Admin-klassen skapar en admin-användare när applikationen startar och styr alla funktionalitet
 */
@Service // Markerar klassen som en servicekomponent, vilket gör att den hanteras av Spring
public class Admin {

    private final UserRepository userRepository; // UserRepository-fält för åtkomst till användardata
    private final PasswordEncoder passwordEncoder; // PasswordEncoder-fält för kryptering av lösenord

    @Autowired // Markerar konstruktören för automatisk injicering av beroenden
    public Admin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; // Tilldelar injicerat UserRepository till fältet
        this.passwordEncoder = passwordEncoder; // Tilldelar injicerat PasswordEncoder till fältet
    }

    @PostConstruct // Anger att metoden ska köras efter att bean-initialiseringen är klar
    public void init() {
        User admin = new User(); // Skapar en ny instans av User-klassen
        admin.setEmail("admin@example.com"); // Sätter e-postadress för admin-användaren
        admin.setPassword(passwordEncoder.encode("admin")); // Krypterar och sätter lösenord för admin-användaren
        admin.setRole("ROLE_ADMIN"); // Sätter rollen för admin-användaren
        userRepository.save(admin); // Sparar admin-användaren i databasen
    }
}