package edu.eci.UniReserva.UniReserva_Backend.jwt;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  private static final long EXPIRATION_TIME = 3600000; // 1 hora
  private final String secretKey;

  public JwtService(@Value("${JWT_SECRET_KEY}") String secretKey) {
    this.secretKey = secretKey;
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
    if (user instanceof User) {
      User appUser = (User) user;
      extraClaims.put("role", appUser.getRole().name());
    }
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateToken(User user) {
    Map<String, Object> extraClaims = new HashMap<>();
    return generateToken(extraClaims, user);
  }

  public String extractUserEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails user) {
    final String userEmail = extractUserEmail(token);
    return userEmail.equals(user.getUsername()) && !isTokenExpired(token);
  }

  public SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Role extractRole(String token) {
    String roleName = extractClaim(token, claims -> claims.get("role", String.class));
    return Role.valueOf(roleName);
  }
}
