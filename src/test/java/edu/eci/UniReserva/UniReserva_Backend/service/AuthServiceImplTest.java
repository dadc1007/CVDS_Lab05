package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private UserRepository userRepository;

    private User validUser;
    private User invalidUser;


    @BeforeEach
    void setUp() {
        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
        invalidUser = new User("1038944351", "Carlos", "invalid@gmail.com", "Password#456");
    }

    @Test
    void testAuthenticateLoginSuccess() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
        User result = authServiceImpl.authenticateLogin(validUser.getEmail(), validUser.getPassword());
        assertEquals("1037126548", result.getId());
        assertEquals("Daniel", result.getName());
        assertEquals("email@gmail.com", result.getEmail());
        assertEquals("Password#123", result.getPassword());
    }

    @Test
    void testAuthenticateLoginUserNotFound() {
        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authServiceImpl.authenticateLogin(invalidUser.getEmail(), invalidUser.getPassword());
        });
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testAuthenticateLoginWrongPassword() {
        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(Optional.of(invalidUser));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authServiceImpl.authenticateLogin(invalidUser.getEmail(), "123");
        });
        assertEquals("Invalid email or password", exception.getMessage());
    }
}
