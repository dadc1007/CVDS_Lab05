package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.ReservationServiceImpl;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    private User validUser;
    private User duplicateEmail;
    private User invalidePassword;
    private User updateName;
    private User updatePassword;

    @BeforeEach
    void setUp() {
        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
        duplicateEmail = new User("1038944351", "Carlos", "email@gmail.com", "Password#456");
        invalidePassword = new User("1038471526", "Vicente", "vicente@gmail.com", "123");
        updateName = new User("1037126548", "Alejandro", "email@gmail.com", "Password#123");
        updatePassword = new User("1037126548", "Daniel", "email@gmail.com", "NewPassword#123");
    }

//    @Test
//    public void shouldCreateUser() {
//        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(validUser);
//
//        User result = userServiceImpl.createUser(validUser);
//
//        assertNotNull(result);
//        assertEquals(validUser, result);
//        verify(userRepository).save(validUser);
//    }

//    @Test
//    public void shouldNotCreateUserWithDuplicatedEmail() {
//        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(validUser));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.createUser(duplicateEmail));
//
//        assertEquals("Email already exists", exception.getMessage());
//        verify(userRepository, never()).save(any(User.class));
//    }

//    @Test
//    public void shouldNotCreateUserWithInvalidPassword() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.createUser(invalidePassword));
//
//        assertEquals("Invalid password", exception.getMessage());
//        verify(userRepository, never()).save(any(User.class));
//    }

    @Test
    public void shouldUpdateName() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.of(validUser));
        when(userRepository.save(any(User.class))).thenReturn(updateName);

        User result = userServiceImpl.updateUser(validUser.getId(), new User(null, "Alejandro", null, null));

        assertNotNull(result);
        assertEquals(updateName, result);
        verify(userRepository).save(argThat(user ->
                user.getId().equals(validUser.getId()) &&
                        user.getName().equals("Alejandro") &&
                        user.getEmail().equals(validUser.getEmail()) &&
                        user.getPassword().equals(validUser.getPassword())
        ));
    }

    @Test
    public void shouldUpdatePassword() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.of(validUser));
        when(userRepository.save(any(User.class))).thenReturn(updatePassword);

        User result = userServiceImpl.updateUser(validUser.getId(), new User(null, null, null, "NewPassword#123"));

        assertNotNull(result);
        assertEquals(updatePassword, result);
        verify(userRepository).save(argThat(user ->
                user.getId().equals(validUser.getId()) &&
                        user.getName().equals(validUser.getName()) &&
                        user.getEmail().equals(validUser.getEmail()) &&
                        user.getPassword().equals("NewPassword#123")
        ));
    }

    @Test
    public void shouldNotUpdateInvalidPassword() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.of(validUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.updateUser(validUser.getId(), new User(null, null, null, "newpassword#123")));

        assertEquals("Invalid password", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldNotUpdateEmail() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.of(validUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.updateUser(validUser.getId(), new User(null, null, "newEmail@gmail.com", null)));

        assertEquals("The email cannot be updated", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldNotUpdateNonExistentUser() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.updateUser(validUser.getId(), new User(null, "Alejandro", null, null)));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserWhenUserExists() {
        String userId = validUser.getId();
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(validUser));
        String result = userServiceImpl.deleteUser(userId);
        verify(userRepository).deleteById(userId);
        assertEquals("User with ID " + userId + " deleted successfully", result);
    }



    @Test
    void shouldNotDeleteUserWhenUserDoesNotExist() {
        String userId = "999";
        when(userRepository.existsById(userId)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.deleteUser(userId));
        assertEquals("User not found",exception.getMessage() );
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldNotDeleteUserWhenHasReservation(){
        String userId = validUser.getId();
        validUser.addReservationId("1234567");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(validUser));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.deleteUser(userId));
        assertEquals("User has Repository, can't be deleted", exception.getMessage());
    }

    @Test
    void shouldGetUserWhenUserExists() {
        when(userRepository.findById(validUser.getId())).thenReturn(Optional.of(validUser));
        User result = userServiceImpl.getUser(validUser.getId());
        assertNotNull(result);
        assertEquals("1037126548", result.getId());
        assertEquals("Daniel", result.getName());
        assertEquals("email@gmail.com",result.getEmail());
        assertEquals("Password#123",result.getPassword());
    }

    @Test
    void shouldNotGetUserWhenUserDoesNotExist() {
        String userId = "999";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userServiceImpl.getUser(userId));
    }


}