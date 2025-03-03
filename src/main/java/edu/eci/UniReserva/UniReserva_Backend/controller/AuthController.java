package edu.eci.UniReserva.UniReserva_Backend.controller;


import edu.eci.UniReserva.UniReserva_Backend.model.LoginRequest;
import edu.eci.UniReserva.UniReserva_Backend.service.AuthService;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String message = authService.authenticateLogin(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(message);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}