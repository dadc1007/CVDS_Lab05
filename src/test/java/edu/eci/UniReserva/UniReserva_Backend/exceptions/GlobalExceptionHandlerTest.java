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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  void shouldReturnInternalServerErrorForGeneralException() throws Exception {
    mockMvc
        .perform(get("/test/general-error"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
  }
}

@RestController
@RequestMapping("/test")
class TestController {

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

  @GetMapping("/general-error")
  public void throwGeneralException() {
    throw new RuntimeException("An unexpected error occurred");
  }
}
