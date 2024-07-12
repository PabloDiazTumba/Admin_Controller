package com.example.login.config; // Anger paketet som denna fil tillhör

import org.springframework.context.annotation.Bean; // Importerar @Bean-annoteringen
import org.springframework.context.annotation.Configuration; // Importerar @Configuration-annoteringen
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importerar HttpSecurity-klassen
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importerar @EnableWebSecurity-annoteringen
import org.springframework.security.web.SecurityFilterChain; // Importerar SecurityFilterChain-klassen

@Configuration // Markerar klassen som en källa för bean-definitioner
@EnableWebSecurity // Aktiverar Spring Securitys webbsäkerhetssupport
public class TestSecurityConfig {

    @Bean // Markerar metoden som en bean, vilket gör att den hanteras av Spring
    public SecurityFilterChain TestsecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Inaktiverar CSRF-skydd
                .authorizeRequests() // Börjar auktoriseringskonfiguration
                .anyRequest().authenticated() // Kräver autentisering för alla förfrågningar
                .and() // Fortsätter konfigurera
                .httpBasic(); // Aktiverar HTTP Basic Authentication
        return http.build(); // Bygger och returnerar SecurityFilterChain
    }
}