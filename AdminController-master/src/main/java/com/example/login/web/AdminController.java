package com.example.login.web; // Anger paketet som denna fil tillhör

import com.example.login.model.User; // Importerar User-klassen
import com.example.login.model.UserDTO; // Importerar UserDTO-klassen
import com.example.login.service.UserService; // Importerar UserService
import com.example.login.util.MaskingUtils; // Importerar MaskingUtils för att maskera e-postadresser
import jakarta.validation.Valid; // Importerar @Valid-annoteringen från Jakarta Validation API
import org.slf4j.Logger; // Importerar SLF4J Logger
import org.slf4j.LoggerFactory; // Importerar SLF4J LoggerFactory
import org.springframework.dao.DataIntegrityViolationException; // Importerar DataIntegrityViolationException från Spring
import org.springframework.security.authentication.BadCredentialsException; // Importerar BadCredentialsException från Spring Security
import org.springframework.security.core.Authentication; // Importerar Authentication från Spring Security
import org.springframework.stereotype.Controller; // Importerar @Controller-annoteringen från Spring
import org.springframework.ui.Model; // Importerar Model från Spring för MVC
import org.springframework.validation.BindingResult; // Importerar BindingResult från Spring för att hantera valideringsresultat
import org.springframework.web.bind.annotation.*; // Importerar annoteringar för att binda HTTP-förfrågningar till metoder
import org.springframework.web.util.HtmlUtils; // Importerar HtmlUtils från Spring Web för att hantera HTML-escape

import java.util.List; // Importerar List-gränssnittet
import java.util.Optional; // Importerar Optional från Java util för att hantera potentiellt tomma värden

/**
 * AdminController hanterar webbförfrågningar relaterade till administration, såsom registrering och borttagning av användare.
 */
@Controller // Markerar klassen som en Controller-komponent för att hantera webbförfrågningar
public class AdminController {

    private final UserService userService; // Deklarerar UserService-fältet för att hantera användartjänster
    private final Logger logger = LoggerFactory.getLogger(AdminController.class); // Skapar en logger för klassen

    // Konstruktor som injicerar UserService
    public AdminController(UserService userService) {
        this.userService = userService; // Tilldelar det injicerade UserService till fältet
    }

    // Hanterar GET-förfrågningar till "/register" och visar registreringsformuläret
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO()); // Lägger till ett nytt UserDTO-objekt i modellen för formuläret
        logger.debug("Register user"); // Loggar att registrering av användare begärs
        return "register_form"; // Returnerar namnet på HTML-sidan för registreringsformuläret
    }

    // Hanterar POST-förfrågningar till "/register" för att registrera en ny användare
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        logger.debug("Processing registration for email: {}", MaskingUtils.anonymize(userDTO.getEmail())); // Loggar behandling av registrering med den maskerade e-postadressen
        if (bindingResult.hasErrors()) { // Kontrollerar om valideringsfel uppstår från användarens inmatning
            logger.warn("Registration failed due to validation errors for email: {}", MaskingUtils.anonymize(userDTO.getEmail())); // Loggar om registrering misslyckas på grund av valideringsfel
            return "register_form"; // Returnerar tillbaka till registreringsformuläret om det finns valideringsfel
        }

        try {
            userService.registerUser(userDTO); // Anropar UserService för att registrera den nya användaren
            logger.debug("User successfully registered with email: {}", MaskingUtils.anonymize(userDTO.getEmail())); // Loggar att användaren har registrerats framgångsrikt
            return "register_success"; // Returnerar namnet på HTML-sidan för framgångsrik registrering
        } catch (DataIntegrityViolationException e) { // Hanterar om användarens e-postadress redan är registrerad
            logger.warn("Registration failed due to email already being registered: {}", MaskingUtils.anonymize(userDTO.getEmail())); // Loggar om registrering misslyckas på grund av att e-postadressen redan är registrerad
            model.addAttribute("error", "Email already registered"); // Lägger till felmeddelande i modellen för att visa på HTML-sidan
            return "register_error"; // Returnerar namnet på HTML-sidan för fel vid registrering
        } catch (Exception e) { // Hanterar andra oväntade fel som kan uppstå under registreringen
            logger.error("An unexpected error occurred during registration for email: {}", MaskingUtils.anonymize(userDTO.getEmail()), e); // Loggar oväntat fel med den maskerade e-postadressen
            model.addAttribute("error", "An unexpected error occurred"); // Lägger till felmeddelande i modellen för att visa på HTML-sidan
            return "register_error"; // Returnerar namnet på HTML-sidan för oväntade fel vid registrering
        }
    }

    // Hanterar GET-förfrågningar till "/homepage" och visar hemsidevyn efter inloggning
    @GetMapping("/homepage")
    public String loggedIn() {
        logger.debug("Homepage"); // Loggar att hemsidevyn visas
        return "homepage"; // Returnerar namnet på HTML-sidan för hemsidevyn
    }

    // Hanterar GET-förfrågningar till "/login" och visar inloggningsformuläret
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("user", new UserDTO()); // Lägger till ett nytt UserDTO-objekt i modellen för inloggningsformuläret
        if (error != null) { // Kontrollerar om det finns ett felmeddelande för inloggning
            model.addAttribute("error", "Incorrect username or password"); // Lägger till felmeddelande i modellen för att visa på HTML-sidan
            logger.debug("Login error - incorrect username or password."); // Loggar att inloggning misslyckades på grund av felaktigt användarnamn eller lösenord
        }
        logger.debug("Showing login page."); // Loggar att inloggningsformuläret visas
        return "login"; // Returnerar namnet på HTML-sidan för inloggningsformuläret
    }

    // Hanterar GET-förfrågningar till "/admin" och visar adminsidan
    @GetMapping("/admin")
    public String adminPage() {
        logger.debug("Admin accessed admin page."); // Loggar att en admin har öppnat adminsidan
        return "admin_page"; // Returnerar namnet på HTML-sidan för adminsidan
    }

    // Hanterar GET-förfrågningar till "/users" och visar sidan med en lista över alla användare
    @GetMapping("/users")
    public String userPage(Model model) {
        List<User> users = userService.findAll(); // Hämtar en lista över alla användare från UserService
        model.addAttribute("users", users); // Lägger till listan med användare i modellen för att visa på HTML-sidan
        logger.debug("Showing usersPage with a list of registered users."); // Loggar att sidan med användarlistan visas
        return "users_list"; // Returnerar namnet på HTML-sidan för användarlistan
    }

    // Hanterar GET-förfrågningar till "/delete" och visar formuläret för att ta bort användare
    @GetMapping("/delete")
    public String deleteUserForm(Model model) {
        logger.debug("Displaying user deletion form."); // Loggar att formuläret för att ta bort användare visas
        model.addAttribute("user", new UserDTO()); // Lägger till ett nytt UserDTO-objekt i modellen för formuläret
        List<User> users = userService.findAll(); // Hämtar en lista över alla användare från UserService
        model.addAttribute("users", users); // Lägger till listan med användare i modellen för att visa på HTML-sidan
        return "delete_form"; // Returnerar namnet på HTML-sidan för formuläret för att ta bort användare
    }

    // Hanterar POST-förfrågningar till "/delete" för att ta bort en användare
    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") UserDTO user, Model model) {
        logger.debug("Processing user deletion."); // Loggar att borttagning av användare pågår

        try {
            Optional<User> userOptional = Optional.ofNullable(userService.findByEmail(HtmlUtils.htmlEscape(user.getEmail()))); // Hämtar användaren baserat på e-postadress
            if (userOptional.isPresent()) { // Kontrollerar om användaren finns
                User user1 = userOptional.get(); // Hämtar användarobjektet från Optional

                if (!user1.getRole().equals("ROLE_ADMIN")) { // Kontrollerar om användaren inte har admin-rollen
                    logger.debug("User does not have the ADMIN role."); // Loggar att användaren inte har admin-rollen

                    userService.deleteUser(user1.getEmail()); // Anropar UserService för att ta bort användaren från databasen

                    logger.debug("User deleted: {}", MaskingUtils.anonymize(user1.getEmail())); // Loggar att användaren har tagits bort med den maskerade e-postadressen
                    return "delete_success"; // Returnerar namnet på HTML-sidan för lyckad borttagning av användare
                } else {
                    logger.warn("ADMIN cannot be deleted."); // Loggar att admin inte kan raderas
                    return "admin_error"; // Returnerar namnet på HTML-sidan för fel vid försök att ta bort admin
                }
            } else {
                logger.warn("User {} not found for deletion.", MaskingUtils.anonymize(user.getEmail())); // Loggar om användaren inte hittades för borttagning
                model.addAttribute("id", HtmlUtils.htmlEscape(user.getEmail())); // Lägger till e-postadressen i modellen för att visa på HTML-sidan
                return "user_not_found"; // Returnerar namnet på HTML-sidan för meddelande om att användaren inte hittades
            }
        } catch (Exception e) { // Hanterar andra oväntade fel som kan uppstå vid borttagning av användare
            logger.error("An error occurred while deleting the user: {}", MaskingUtils.anonymize(user.getEmail()), e); // Loggar oväntat fel med den maskerade e-postadressen
            model.addAttribute("error", "An error occurred while deleting the user."); // Lägger till felmeddelande i modellen för att visa på HTML-sidan
            return "delete_error"; // Returnerar namnet på HTML-sidan för fel vid borttagning av användare
        }
    }

    // Hanterar GET-förfrågningar till "/delete_success" och visar sidan för lyckad borttagning av användare
    @GetMapping("/delete_success")
    public String deleteSuccess() {
        logger.debug("User deleted successfully."); // Loggar att användaren har tagits bort framgångsrikt
        return "delete_success"; // Returnerar namnet på HTML-sidan för lyckad borttagning av användare
    }

    // Hanterar GET-förfrågningar till "/delete-error" och visar sidan för fel vid borttagning av användare
    @GetMapping("/delete-error")
    public String deleteError() {
        logger.debug("Error during user deletion."); // Loggar att ett fel inträffade vid borttagning av användare
        return "delete_error"; // Returnerar namnet på HTML-sidan för fel vid borttagning av användare
    }

}