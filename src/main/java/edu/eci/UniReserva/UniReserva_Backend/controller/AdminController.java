package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.RegisterUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.AuthService;
import org.springframework.http.HttpStatus;
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
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/createUser")
    public  ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody RegisterUserDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createAdmin(request));
    }
}