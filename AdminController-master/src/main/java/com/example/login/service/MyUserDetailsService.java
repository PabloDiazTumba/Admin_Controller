package com.example.login.service; // Anger paketet som denna fil tillhör

import com.example.login.model.User; // Importerar User-klassen
import com.example.login.model.UserRepository; // Importerar UserRepository-gränssnittet
import org.springframework.security.core.GrantedAuthority; // Importerar GrantedAuthority-gränssnittet
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importerar SimpleGrantedAuthority-klassen
import org.springframework.security.core.userdetails.UserDetails; // Importerar UserDetails-gränssnittet
import org.springframework.security.core.userdetails.UserDetailsService; // Importerar UserDetailsService-gränssnittet
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importerar UsernameNotFoundException-klassen
import org.springframework.stereotype.Service; // Importerar @Service-annoteringen

import java.util.Collection; // Importerar Collection-gränssnittet
import java.util.Collections; // Importerar Collections-klassen

/**
 * MyUserDetailsService laddar användardetaljer från databasen vid inloggning.
 */
@Service // Markerar klassen som en servicekomponent, vilket gör att den hanteras av Spring
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Deklarerar ett fält för UserRepository

    // Konstruktor som injicerar UserRepository
    public MyUserDetailsService(UserRepository userRepository) {
        super(); // Anropar basklassens konstruktor
        this.userRepository = userRepository; // Tilldelar det injicerade UserRepository till fältet
    }

    // Implementerar loadUserByUsername-metoden från UserDetailsService-gränssnittet
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hittar användaren baserat på e-postadress
        User user = userRepository.findByEmail(username);
        if (user == null) { // Om användaren inte hittas, kasta ett undantag
            throw new UsernameNotFoundException("No user found with email: " + username);
        }
        // Hämtar användarens auktoriteter (roller)
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRole());
        // Returnerar en ny instans av UserDetails med användarens detaljer och auktoriteter
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
    }

    // Hjälpmetod för att hämta användarens auktoriteter baserat på roll
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role)); // Returnerar en lista med en enkel roll
    }
}