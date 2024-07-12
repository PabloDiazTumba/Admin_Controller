package com.example.login.service; // Anger paketet som denna fil tillhör

import com.example.login.model.User; // Importerar User-klassen
import com.example.login.model.UserDTO; // Importerar UserDTO-klassen
import com.example.login.model.UserRepository; // Importerar UserRepository-gränssnittet
import org.slf4j.Logger; // Importerar SLF4J Logger
import org.slf4j.LoggerFactory; // Importerar SLF4J LoggerFactory
import org.springframework.beans.factory.annotation.Autowired; // Importerar @Autowired-annoteringen
import org.springframework.security.crypto.password.PasswordEncoder; // Importerar PasswordEncoder-gränssnittet
import org.springframework.stereotype.Service; // Importerar @Service-annoteringen

import java.util.List; // Importerar List-gränssnittet

/**
 * UserServiceImpl implementerar UserService och hanterar användartjänster, såsom att hitta, registrera och ta bort användare.
 */
@Service // Markerar klassen som en servicekomponent, vilket gör att den hanteras av Spring
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Deklarerar ett fält för UserRepository
    private final PasswordEncoder passwordEncoder; // Deklarerar ett fält för PasswordEncoder
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); // Skapar en logger för klassen

    // Konstruktor som injicerar UserRepository och PasswordEncoder
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; // Tilldelar det injicerade UserRepository till fältet
        this.passwordEncoder = passwordEncoder; // Tilldelar det injicerade PasswordEncoder till fältet
    }

    // Implementerar findAll-metoden från UserService-gränssnittet
    @Override
    public List<User> findAll() {
        return userRepository.findAll(); // Anropar UserRepository för att hämta alla användare
    }

    // Implementerar deleteUser-metoden från UserService-gränssnittet
    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email); // Hämtar användaren baserat på e-postadress
        if (user != null) { // Om användaren finns
            userRepository.delete(user); // Tar bort användaren från databasen
        } else {
            throw new RuntimeException("User not found"); // Kastar ett undantag om användaren inte hittas
        }
    }

    // Implementerar registerUser-metoden från UserService-gränssnittet
    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User(); // Skapar en ny instans av User
        user.setEmail(userDTO.getEmail()); // Sätter e-postadressen från UserDTO
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Krypterar och sätter lösenordet från UserDTO
        user.setRole("ROLE_USER"); // Sätter rollen till standardvärde "ROLE_USER"
        User savedUser = userRepository.save(user); // Sparar användaren i databasen och får tillbaka den sparade instansen
        logger.debug("User saved with ID: {}", savedUser.getId()); // Loggar användarens ID vid sparande
        return savedUser; // Returnerar den sparade användaren
    }

    // Implementerar findByEmail-metoden från UserService-gränssnittet
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email); // Returnerar användaren baserat på e-postadress
    }
}