package com.example.login.util; // Anger paketet som denna fil tillhör

/**
 * MaskingUtils innehåller verktygsmetoder för att maskera e-postadresser.
 */
public class MaskingUtils {

    /**
     * Metoden anonymize tar en e-postadress och ersätter delar av den med asterisker för att maskera den.
     * Om e-postadressen är ogiltig eller inte kan maskeras returneras den ursprungliga e-postadressen.
     *
     * @param email den e-postadress som ska maskeras
     * @return den maskerade e-postadressen eller den ursprungliga e-postadressen om den inte kan maskeras
     */
    public static String anonymize(String email) {
        if (email == null || email.isEmpty()) { // Kontrollerar om e-postadressen är null eller tom
            return ""; // Returnerar en tom sträng om e-postadressen är ogiltig
        }
        String[] parts = email.split("@"); // Delar upp e-postadressen vid "@"-tecknet
        if (parts.length != 2) { // Kontrollerar att e-postadressen innehåller precis ett "@"-tecken
            return email; // Returnerar den ursprungliga e-postadressen om den inte kan delas upp korrekt
        }
        String username = parts[0]; // Hämtar användarnamnet från e-postadressen
        if (username.length() <= 1) { // Kontrollerar om användarnamnet är för kort för att maskeras
            return email; // Returnerar den ursprungliga e-postadressen om användarnamnet inte kan maskeras
        }
        // Konstruerar den maskerade e-postadressen med asterisker
        return username.charAt(0) + "**********" + username.charAt(username.length() - 1) + "@" + parts[1];
    }
}