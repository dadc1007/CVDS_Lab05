package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ApiResponse<UserDto> updateUser(String id, User user) {
    User existingUser =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (user.getEmail() != null) {
      throw new IllegalArgumentException("The email cannot be updated");
    }

    if (user.getName() != null) {
      existingUser.setName(user.getName());
    }

    if (user.getPassword() != null) {
      if (!validPassword(user.getPassword())) {
        throw new IllegalArgumentException("Invalid password");
      }
      existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    userRepository.save(existingUser);

    return ApiResponse.<UserDto>builder()
        .status("success")
        .message("User updated successfully")
        .data(toUserDTO(existingUser))
        .build();
  }

  @Override
  public ApiResponse<UserDto> deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new IllegalArgumentException("User not found");
    }

    if (hasRepository(id)) {
      throw new IllegalArgumentException("User has Repository, can't be deleted");
    }

    userRepository.deleteById(id);

    return ApiResponse.<UserDto>builder().status("success").message("User deleted").build();
  }

  @Override
  public ApiResponse<UserDto> getUser(String id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return ApiResponse.<UserDto>builder()
        .status("success")
        .message("User requested found")
        .data(toUserDTO(user))
        .build();
  }

  private boolean validPassword(String password) {
    return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
  }

  private boolean hasRepository(String id) {
    User usuario =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    if (usuario.getReservations() == null) {
      return false;
    }
    if (!(usuario.getReservations().size() > 0)) {
      return false;
    }
    return true;
  }

  private UserDto toUserDTO(User user) {
    return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getReservations(),user.getRole());
  }
}
