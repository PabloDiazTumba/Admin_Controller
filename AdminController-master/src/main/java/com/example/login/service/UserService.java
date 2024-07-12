package com.example.login.service; // Anger paketet som denna fil tillhör

import com.example.login.model.User; // Importerar User-klassen
import com.example.login.model.UserDTO; // Importerar UserDTO-klassen

import java.util.List; // Importerar List-gränssnittet

/**
 * UserService är ett gränssnitt för användartjänster, såsom att hitta, registrera och ta bort användare.
 */
public interface UserService {
    // Metod för att hämta en lista över alla användare
    List<User> findAll();

    // Metod för att ta bort en användare baserat på e-postadress
    void deleteUser(String email);

    // Metod för att registrera en ny användare baserat på UserDTO
    User registerUser(UserDTO userDTO);

    // Metod för att hitta en användare baserat på e-postadress
    User findByEmail(String email);
}