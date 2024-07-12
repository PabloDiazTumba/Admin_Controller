package com.example.login;

import com.example.login.config.TestSecurityConfig;
import com.example.login.model.User;
import com.example.login.service.UserService;
import com.example.login.web.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = AdminController.class)  // Ange kontrollern som ska testas
@Import(TestSecurityConfig.class)  // Importera konfigurationen för säkerhet
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc för att testa webblager

    @MockBean
    private UserService userService;  // MockBean för användartjänsten

    @BeforeEach
    public void setUp() {
        Mockito.reset(userService); // Återställ mock-objektet före varje test
    }

    @Test
    @WithMockUser
    public void testHomepageAuthenticated() throws Exception {
        mockMvc.perform(get("/homepage"))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("homepage"));  // Förväntar oss att vyn är "homepage"
    }

    @Test
    public void testHomepageNotAuthenticated() throws Exception {
        mockMvc.perform(get("/homepage"))
                .andExpect(status().isUnauthorized());  // Förväntar oss att användaren inte är auktoriserad (401)
    }

    @Test
    @WithMockUser
    public void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("register_success"));  // Förväntar oss att vyn är "register_success"
    }

    @Test
    @WithMockUser
    public void testRegistrationValidationError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("register_form"));  // Förväntar oss att vyn är "register_form"
    }

    @Test
    @WithMockUser
    public void testDeleteUserSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole("ROLE_USER");
        Mockito.when(userService.findByEmail(anyString())).thenReturn(user);  // Mocka UserService för att returnera användaren

        mockMvc.perform(post("/delete")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("delete_success"));  // Förväntar oss att vyn är "delete_success"
    }

    @Test
    @WithMockUser
    public void testDeleteUserNotFound() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(null);  // Mocka UserService för att returnera null

        mockMvc.perform(post("/delete")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("user_not_found"));  // Förväntar oss att vyn är "user_not_found"
    }

    @Test
    @WithMockUser
    public void testDeleteAdminUser() throws Exception {
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole("ROLE_ADMIN");
        Mockito.when(userService.findByEmail(anyString())).thenReturn(adminUser);  // Mocka UserService för att returnera admin-användaren

        mockMvc.perform(post("/delete")
                        .param("email", "admin@example.com"))
                .andExpect(status().isOk())  // Förväntar oss status OK (200)
                .andExpect(view().name("admin_error"));  // Förväntar oss att vyn är "admin_error"
    }

}