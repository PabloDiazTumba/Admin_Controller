package com.example.login.model; // Anger paketet som denna fil tillhör

import jakarta.validation.constraints.Email; // Importerar @Email-annoteringen för att validera e-postadresser
import jakarta.validation.constraints.NotBlank; // Importerar @NotBlank-annoteringen för att validera att fält inte är tomma
import jakarta.validation.constraints.Size; // Importerar @Size-annoteringen för att validera fältets storlek
import lombok.Getter; // Importerar Lombok-annoteringen @Getter för att generera getter-metoder
import lombok.Setter; // Importerar Lombok-annoteringen @Setter för att generera setter-metoder

import java.io.Serializable; // Importerar Serializable-gränssnittet

/**
 * UserDTO är ett dataöverföringsobjekt som används för att validera användarens inmatning vid registrering.
 */
@Setter // Genererar automatiskt setter-metoder för alla fält
@Getter // Genererar automatiskt getter-metoder för alla fält
public class UserDTO implements Serializable { // Implementerar Serializable-gränssnittet för att möjliggöra serialisering

  @NotBlank(message = "Email is required") // Validerar att e-postadressen inte är tom och anger ett felmeddelande om den är tom
  @Email(message = "Please provide a valid email") // Validerar att e-postadressen är giltig och anger ett felmeddelande om den inte är giltig
  private String email; // Deklarerar fältet email

  @NotBlank(message = "Password is required") // Validerar att lösenordet inte är tomt och anger ett felmeddelande om det är tomt
  @Size(min = 6, message = "Password must be at least 6 characters") // Validerar att lösenordet är minst 6 tecken långt och anger ett felmeddelande om det inte är det
  private String password; // Deklarerar fältet password
}