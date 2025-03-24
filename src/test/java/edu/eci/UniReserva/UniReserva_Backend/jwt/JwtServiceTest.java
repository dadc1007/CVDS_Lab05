package edu.eci.UniReserva.UniReserva_Backend.jwt;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private JwtService jwtService;
  private final String secretKey = "MySuperSecretKeyForJwtTestingMySuperSecretKeyForJwtTesting";
  private User user;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(secretKey);
    user = new User();
    user.setId("1");
    user.setEmail("test@example.com");
    user.setRol(Role.PROFESOR);
  }

  @Test
  void shouldGenerateTokenSuccessfully() {
    String token = jwtService.generateToken(user);
    assertNotNull(token);
  }

  @Test
  void shouldExtractUserEmailFromToken() {
    String token = jwtService.generateToken(user);
    String extractedEmail = jwtService.extractUserEmail(token);
    assertEquals(user.getEmail(), extractedEmail);
  }

  @Test
  void shouldExtractRoleFromToken() {
    String token = jwtService.generateToken(user);
    Role extractedRole = jwtService.extractRole(token);
    assertEquals(user.getRole(), extractedRole);
  }

  @Test
  void shouldValidateTokenSuccessfully() {
    String token = jwtService.generateToken(user);
    assertTrue(jwtService.isTokenValid(token, user));
  }

  @Test
  void shouldInvalidateExpiredToken() {
    String expiredToken = generateExpiredToken(user);

    assertThrows(
        ExpiredJwtException.class,
        () -> {
          jwtService.isTokenValid(expiredToken, user);
        });
  }

  // Generar un token expirado
  private String generateExpiredToken(UserDetails user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", ((User) user).getRole().name());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
        .setExpiration(new Date(System.currentTimeMillis() - 3600000))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private SecretKey getKey() {
    return jwtService.getKey();
  }
}
