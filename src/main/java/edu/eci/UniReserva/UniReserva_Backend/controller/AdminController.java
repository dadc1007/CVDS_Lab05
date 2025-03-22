package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.RegisterUserDto;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Restringe acceso solo a ADMIN
public class AdminController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody RegisterUserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User newUser = new User(
                request.getId(),
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "ADMIN");

        userRepository.save(newUser);

        return ResponseEntity.ok("User created successfully");
    }
}