package edu.eci.UniReserva.UniReserva_Backend.exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.eci.UniReserva.UniReserva_Backend.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private JwtService jwtService;

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(globalExceptionHandler)
            .build();
  }

  @Test
  void shouldReturnUnauthorizedForBadCredentialsException() throws Exception {
    mockMvc
        .perform(get("/test/bad-credentials"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value(UniReservaException.INVALID_PASSWORD_EMAIL));
  }

  @Test
  void shouldReturnForbiddenForAccountStatusException() throws Exception {
    mockMvc
        .perform(get("/test/account-status"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("Forbidden"))
        .andExpect(jsonPath("$.message").value("The account is locked"));
  }

  @Test
  void shouldReturnForbiddenForAccessDeniedException() throws Exception {
    mockMvc
        .perform(get("/test/access-denied"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("Forbidden"))
        .andExpect(jsonPath("$.message").value("You are not authorized to access this resource"));
  }

  @Test
  void shouldReturnForbiddenForSignatureException() throws Exception {
    mockMvc
        .perform(get("/test/signature"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("Invalid Token"))
        .andExpect(jsonPath("$.message").value("The JWT signature is invalid"));
  }

  @Test
  void shouldReturnForbiddenForExpiredJwtException() throws Exception {
    mockMvc
        .perform(get("/test/expired-jwt"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("Token Expired"))
        .andExpect(jsonPath("$.message").value("The JWT token has expired"));
  }

  @Test
  void shouldReturnBadRequestForIllegalArgumentException() throws Exception {
    String errorMessage = "Invalid argument provided";

    mockMvc
        .perform(get("/test/illegal-argument").param("message", errorMessage))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void shouldReturnInternalServerErrorForGeneralException() throws Exception {
    mockMvc
        .perform(get("/test/general-error"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
  }

  @Test
  void shouldReturnCorrectStatusForAllUniReservaExceptions() throws Exception {
    Map<String, HttpStatus> errorMessages = new HashMap<>();

    errorMessages.put(UniReservaException.INVALID_PASSWORD_EMAIL, HttpStatus.UNAUTHORIZED);
    errorMessages.put(UniReservaException.EMAIL_EXISTS, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.LAB_NOT_FOUND, HttpStatus.NOT_FOUND);
    errorMessages.put(UniReservaException.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    errorMessages.put(UniReservaException.INVALID_TIME, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.INVALID_DATE, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.INVALID_RESERVATION, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.INVALID_PRIORITY, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.RESERVATION_NOT_FOUND, HttpStatus.NOT_FOUND);
    errorMessages.put(UniReservaException.RESERVATION_ALREADY_CANCELED, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.START_DATE_BEFORE_END_DATE, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.EMAIL_CANNOT_BE_UPDATED, HttpStatus.BAD_REQUEST);
    errorMessages.put(UniReservaException.USER_CANNOT_BE_DELETED, HttpStatus.BAD_REQUEST);

    for (Map.Entry<String, HttpStatus> entry : errorMessages.entrySet()) {
      String errorMessage = entry.getKey();
      HttpStatus expectedStatus = entry.getValue();

      mockMvc
          .perform(get("/test/unireserva-exception").param("message", errorMessage))
          .andExpect(status().is(expectedStatus.value()))
          .andExpect(jsonPath("$.error").value(expectedStatus.getReasonPhrase()))
          .andExpect(jsonPath("$.message").value(errorMessage));
    }
  }
}

@RestController
@RequestMapping("/test")
class TestController {
  @GetMapping("/bad-credentials")
  public void throwBadCredentialsException() {
    throw new BadCredentialsException("Invalid username or password");
  }

  @GetMapping("/account-status")
  public void throwAccountStatusException() {
    throw new AccountStatusException("The account is locked") {};
  }

  @GetMapping("/access-denied")
  public void throwAccessDeniedException() {
    throw new AccessDeniedException("You are not authorized to access this resource");
  }

  @GetMapping("/signature")
  public void throwSignatureException() {
    throw new SignatureException("The JWT signature is invalid");
  }

  @GetMapping("/expired-jwt")
  public void throwExpiredJwtException() {
    throw new ExpiredJwtException(null, null, "The JWT token has expired");
  }

  @GetMapping("/illegal-argument")
  public void throwIllegalArgumentException(@RequestParam String message) {
    throw new IllegalArgumentException(message);
  }

  @GetMapping("/general-error")
  public void throwGeneralException() {
    throw new RuntimeException("An unexpected error occurred");
  }

  @GetMapping("/unireserva-exception")
  public void throwUniReservaException(@RequestParam String message) {
    throw new UniReservaException(message);
  }
}
