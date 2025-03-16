package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import edu.eci.UniReserva.UniReserva_Backend.jwt.JwtService;
import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.*;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Override
  public ApiResponse<UserDto> authenticateLogin(LoginUserDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    return ApiResponse.<UserDto>builder()
        .status("success")
        .message("login successfully")
        .data(toUserDTO(user))
        .token(jwtService.generateToken(user))
        .build();
  }

  @Override
  public ApiResponse<UserDto> authenticateSignUp(RegisterUserDto request) {
    if (emailExists(request.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }

    if (!validPassword(request.getPassword())) {
      throw new IllegalArgumentException("Invalid password");
    }

    User user =
        new User(
            request.getId(),
            request.getName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);

    return ApiResponse.<UserDto>builder()
        .status("success")
        .message("signup successfully")
        .data(toUserDTO(user))
        .token(jwtService.generateToken(user))
        .build();
  }

  private boolean emailExists(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  private boolean validPassword(String password) {
    return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
  }

  private UserDto toUserDTO(User user) {
    return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getReservations());
  }
}
