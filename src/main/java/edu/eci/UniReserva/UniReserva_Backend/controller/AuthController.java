package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.LoginUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.RegisterUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginUserDto request) {
    return ResponseEntity.ok(authService.authenticateLogin(request));
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<UserDto>> signup(@RequestBody RegisterUserDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.authenticateSignUp(request));
  }
}
