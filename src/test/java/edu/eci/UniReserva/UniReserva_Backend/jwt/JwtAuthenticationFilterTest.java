package edu.eci.UniReserva.UniReserva_Backend.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;

class JwtAuthenticationFilterTest {

  @Mock private JwtService jwtService;
  @Mock private UserDetailsService userDetailsService;
  @Mock private UserDetails userDetails;

  @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldContinueFilterIfNoTokenPresent() throws ServletException, IOException {
    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldNotAuthenticateIfTokenIsInvalid() throws ServletException, IOException {
    request.addHeader("Authorization", "Bearer invalid_token");

    when(jwtService.extractUserEmail("invalid_token")).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldAuthenticateIfTokenIsValid() throws ServletException, IOException {
    String token = "valid_token";
    String email = "test@example.com";

    request.addHeader("Authorization", "Bearer " + token);

    when(jwtService.extractUserEmail(token)).thenReturn(email);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
    when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
    when(userDetails.getAuthorities()).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    assertTrue(
        SecurityContextHolder.getContext().getAuthentication()
            instanceof UsernamePasswordAuthenticationToken);
  }
}
