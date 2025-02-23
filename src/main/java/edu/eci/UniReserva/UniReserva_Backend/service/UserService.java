package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(User user) {
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!validPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        userRepository.save(user);

        return "User created successfully!";
    }

    public String updateUser(String id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        if (user.getEmail() != null) {
            throw new IllegalArgumentException("The email cannot be updated");
        }

        if (user.getName() != null) {
            userOptional.get().setName(user.getName());
        }

        if (user.getPassword() != null) {
            if (!validPassword(user.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }

            userOptional.get().setPassword(user.getPassword());
        }

        userRepository.save(userOptional.get());

        return "User updated successfully!";
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean validPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    }
}
