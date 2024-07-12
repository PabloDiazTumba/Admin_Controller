package com.example.login.config; // Anger paketet som denna fil tillhör

import com.example.login.model.UserRepository; // Importerar UserRepository-klassen
import com.example.login.service.MyUserDetailsService; // Importerar MyUserDetailsService-klassen
import org.springframework.context.annotation.Bean; // Importerar @Bean-annoteringen
import org.springframework.context.annotation.Configuration; // Importerar @Configuration-annoteringen
import org.springframework.security.authentication.AuthenticationManager; // Importerar AuthenticationManager-klassen
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // Importerar AuthenticationManagerBuilder-klassen
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importerar HttpSecurity-klassen
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importerar @EnableWebSecurity-annoteringen
import org.springframework.security.core.userdetails.UserDetailsService; // Importerar UserDetailsService-gränssnittet
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importerar BCryptPasswordEncoder-klassen
import org.springframework.security.crypto.password.PasswordEncoder; // Importerar PasswordEncoder-gränssnittet
import org.springframework.security.web.SecurityFilterChain; // Importerar SecurityFilterChain-klassen

/**
 * SecurityConfig är klassen som konfigurerar säkerheten för vår applikation.
 * Här ställer vi in hur inloggning, utloggning och tillgång till olika sidor ska fungera.
 */
@Configuration // Markerar klassen som en källa för bean-definitioner
@EnableWebSecurity // Aktiverar Spring Securitys webbsäkerhetssupport
public class SecurityConfig {

   /**
    * Skapar en AuthenticationManager som hanterar användarens inloggning med hjälp av deras detaljer och lösenord.
    */
   @Bean // Markerar metoden som en bean, vilket gör att den hanteras av Spring
   public AuthenticationManager authManager(HttpSecurity http, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
      return http.getSharedObject(AuthenticationManagerBuilder.class) // Hämtar AuthenticationManagerBuilder från HttpSecurity
              .userDetailsService(userDetailsService) // Ställer in UserDetailsService
              .passwordEncoder(passwordEncoder) // Ställer in PasswordEncoder
              .and() // Fortsätter konfigurera
              .build(); // Bygger och returnerar AuthenticationManager
   }

   /**
    * Skapar en SecurityFilterChain som innehåller alla våra säkerhetsinställningar.
    */
   @Bean // Markerar metoden som en bean, vilket gör att den hanteras av Spring
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Inaktiverar CSRF-skydd för /h2-console/**-vägarna
              .authorizeHttpRequests(authorizeRequests -> // Börjar auktoriseringskonfiguration
                      authorizeRequests
                              .requestMatchers("/login", "/logout", "/register", "/h2-console/**").permitAll() // Tillåter dessa vägar utan autentisering
                              .requestMatchers("/admin/**").hasRole("ADMIN") // Kräver ADMIN-roll för /admin/**
                              .requestMatchers("/users", "/delete").hasRole("ADMIN") // Kräver ADMIN-roll för /users och /delete
                              .anyRequest().authenticated() // Kräver autentisering för alla andra förfrågningar
              )
              .formLogin(formLogin -> // Konfigurerar formulärinloggning
                      formLogin
                              .loginPage("/login") // Anger anpassad inloggningssida
                              .defaultSuccessUrl("/homepage", true) // Anger standard URL vid lyckad inloggning
                              .failureUrl("/login?error=true") // Anger URL vid misslyckad inloggning
                              .permitAll() // Tillåter alla att se inloggningssidan
              )
              .logout(logout -> logout // Konfigurerar utloggning
                      .logoutUrl("/perform_logout") // Anger URL för utloggningsbegäran
                      .logoutSuccessUrl("/login") // Anger URL efter lyckad utloggning
                      .permitAll()) // Tillåter alla att logga ut
              .headers(headers -> headers.frameOptions().disable()); // Inaktiverar X-Frame-Options-headers för att möjliggöra visning av H2-konsolen

      return http.build(); // Bygger och returnerar SecurityFilterChain
   }

   /**
    * Skapar en PasswordEncoder klass som använder BCrypt-algoritmen för att kryptera lösenord.
    */
   @Bean // Markerar metoden som en bean, vilket gör att den hanteras av Spring
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(); // Returnerar en ny instans av BCryptPasswordEncoder
   }

   /**
    * Skapar en UserDetailsService som hanterar inloggningsinformation för användare.
    */
   @Bean // Markerar metoden som en bean, vilket gör att den hanteras av Spring
   public UserDetailsService userDetailsService(UserRepository userRepository) {
      return new MyUserDetailsService(userRepository); // Returnerar en ny instans av MyUserDetailsService med UserRepository som parameter
   }
}