package edu.eci.UniReserva.UniReserva_Backend.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentialsException(
      BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(
            Map.of("error", "Unauthorized", "message", UniReservaException.INVALID_PASSWORD_EMAIL));
  }

  @ExceptionHandler(AccountStatusException.class)
  public ResponseEntity<Map<String, String>> handleAccountStatusException(
      AccountStatusException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            Map.of(
                "error", "Forbidden",
                "message", "The account is locked"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            Map.of(
                "error", "Forbidden",
                "message", "You are not authorized to access this resource"));
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<Map<String, String>> handleSignatureException(SignatureException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            Map.of(
                "error", "Invalid Token",
                "message", "The JWT signature is invalid"));
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            Map.of(
                "error", "Token Expired",
                "message", "The JWT token has expired"));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "Bad Request", "message", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            Map.of(
                "error", "Internal Server Error",
                "message", "An unexpected error occurred"));
  }

  @ExceptionHandler(UniReservaException.class)
  public ResponseEntity<Map<String, String>> handleUniReservaException(UniReservaException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    if (ex.getMessage().equals(UniReservaException.INVALID_PASSWORD_EMAIL)) {
      status = HttpStatus.UNAUTHORIZED;
    } else if (ex.getMessage().equals(UniReservaException.LAB_NOT_FOUND)
        || ex.getMessage().equals(UniReservaException.USER_NOT_FOUND)
        || ex.getMessage().equals(UniReservaException.RESERVATION_NOT_FOUND)) {
      status = HttpStatus.NOT_FOUND;
    }

    return ResponseEntity.status(status)
        .body(Map.of("error", status.getReasonPhrase(), "message", ex.getMessage()));
  }
}
