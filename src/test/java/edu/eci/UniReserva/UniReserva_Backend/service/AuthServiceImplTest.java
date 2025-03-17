package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.jwt.JwtService;
import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.LoginUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.RegisterUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    private User validUser;
    private LoginUserDto validLoginUserDto;
    private User invalidUser;


    @BeforeEach
    void setUp() {
        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
        validLoginUserDto = new LoginUserDto("email@gmail.com", "Password#123");
        invalidUser = new User("1038944351", "Carlos", "invalid@gmail.com", "Password#456");
    }

    @Test
    void testAuthenticateLoginSuccess() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user", "password"));
        ApiResponse<UserDto> response = authServiceImpl.authenticateLogin(validLoginUserDto);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("login successfully", response.getMessage());
        assertEquals("mockedToken", response.getToken());

        assertNotNull(response.getData());
        assertEquals(validUser.getId(), response.getData().getId());
        assertEquals(validUser.getName(), response.getData().getName());
        assertEquals(validUser.getEmail(), response.getData().getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(validUser.getEmail());
    }

    @Test
    void testAuthenticateLoginUserNotFound() {
        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authServiceImpl.authenticateLogin(new LoginUserDto("invalid@gmail.com", "Password#123"));
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testAuthenticateLoginWrongPassword() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));
        LoginUserDto invalidPasswordDto = new LoginUserDto("invalid@gmail.com", "123");
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authServiceImpl.authenticateLogin(invalidPasswordDto);
        });
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void shouldSignUpUser() {
        RegisterUserDto registerUserDto = new RegisterUserDto("12345", "John Doe", "john@example.com", "Password123!");
        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ApiResponse<UserDto> response = authServiceImpl.authenticateSignUp(registerUserDto);
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("signup successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(registerUserDto.getEmail(), response.getData().getEmail());
        assertEquals("mocked-jwt-token", response.getToken());
    }

    @Test
    public void shouldNotSingUpUserWithDuplicatedEmail() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
        RegisterUserDto registerUserDto = new RegisterUserDto("12345", "John Doe", "email@gmail.com", "Password123!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.authenticateSignUp(registerUserDto));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldNotSingUpUserWithInvalidPassword() {
        RegisterUserDto registerUserDto = new RegisterUserDto("12345", "John Doe", "email@gmail.com", "123!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.authenticateSignUp(registerUserDto));
        assertEquals("Invalid password", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
