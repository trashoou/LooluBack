package loolu.loolu_backend;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.dto.LoginRequestDto;
import loolu.loolu_backend.security.sec_controller.AuthController;
import loolu.loolu_backend.security.sec_dto.TokenResponseDto;
import loolu.loolu_backend.security.sec_service.AuthService;
import loolu.loolu_backend.services.impl.UserService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@AutoConfigureMockMvc
@DisplayName("AuthController tests:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@Rollback
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Login - Success")
    void testLogin_Success() {
        LoginRequestDto loginRequest = new LoginRequestDto("user@example.com", "Password123!");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.isEnabled();

        TokenResponseDto tokenResponse = new TokenResponseDto("accessToken", "refreshToken");

        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        try {
            when(authService.login(loginRequest)).thenReturn(tokenResponse);
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<TokenResponseDto> responseEntity = authController.login(loginRequest, response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tokenResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Login - User not found")
    void testLogin_UserNotFound() {
        LoginRequestDto loginRequest = new LoginRequestDto("user@example.com", "Password123!");

        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(null);

        ResponseEntity<TokenResponseDto> responseEntity = authController.login(loginRequest, response);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new TokenResponseDto(null, null), responseEntity.getBody());
    }
    @Test
    @DisplayName("Login - User not enabled")
    void testLogin_UserNotEnabled() {
        LoginRequestDto loginRequest = new LoginRequestDto("user@example.com", "Password123!");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.isEnabled();

        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(user);

        ResponseEntity<TokenResponseDto> responseEntity = authController.login(loginRequest, response);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(new TokenResponseDto(null, null), responseEntity.getBody());
    }

    @Test
    @DisplayName("Login - Password mismatch")
    void testLogin_PasswordMismatch() {
        LoginRequestDto loginRequest = new LoginRequestDto("user@example.com", "Password123!");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.isEnabled();

        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        ResponseEntity<TokenResponseDto> responseEntity = authController.login(loginRequest, response);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(new TokenResponseDto(null, null), responseEntity.getBody());
    }

}
