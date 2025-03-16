//package edu.eci.UniReserva.UniReserva_Backend.service;
//
//import edu.eci.UniReserva.UniReserva_Backend.model.User;
//import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
//import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthServiceImplTest {
//
//    @InjectMocks
//    private AuthServiceImpl authServiceImpl;
//
//    @Mock
//    private UserRepository userRepository;
//
//    private User validUser;
//    private User invalidUser;
//    private User duplicateEmail;
//    private User invalidePassword;
//
//
//    @BeforeEach
//    void setUp() {
//        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
//        invalidUser = new User("1038944351", "Carlos", "invalid@gmail.com", "Password#456");
//        duplicateEmail = new User("1038944351", "Carlos", "email@gmail.com", "Password#456");
//        invalidePassword = new User("1038471526", "Vicente", "vicente@gmail.com", "123");
//    }
//
//    @Test
//    void testAuthenticateLoginSuccess() {
//        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
//        User result = authServiceImpl.authenticateLogin(validUser.getEmail(), validUser.getPassword());
//        assertEquals("1037126548", result.getId());
//        assertEquals("Daniel", result.getName());
//        assertEquals("email@gmail.com", result.getEmail());
//        assertEquals("Password#123", result.getPassword());
//    }
//
//    @Test
//    void testAuthenticateLoginUserNotFound() {
//        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(Optional.empty());
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            authServiceImpl.authenticateLogin(invalidUser.getEmail(), invalidUser.getPassword());
//        });
//        assertEquals("Invalid email or password", exception.getMessage());
//    }
//
//    @Test
//    void testAuthenticateLoginWrongPassword() {
//        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(Optional.of(invalidUser));
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            authServiceImpl.authenticateLogin(invalidUser.getEmail(), "123");
//        });
//        assertEquals("Invalid email or password", exception.getMessage());
//    }
//
//    @Test
//    public void shouldSingUpUser() {
//        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(validUser);
//
//        User result = authServiceImpl.authenticateSignUp(validUser);
//
//        assertNotNull(result);
//        assertEquals(validUser, result);
//        verify(userRepository).save(validUser);
//    }
//
//    @Test
//    public void shouldNotSingUpUserWithDuplicatedEmail() {
//        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.authenticateSignUp(duplicateEmail));
//
//        assertEquals("Email already exists", exception.getMessage());
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    public void shouldNotSingUpUserWithInvalidPassword() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.authenticateSignUp(invalidePassword));
//        assertEquals("Invalid password", exception.getMessage());
//        verify(userRepository, never()).save(any(User.class));
//    }
//}
