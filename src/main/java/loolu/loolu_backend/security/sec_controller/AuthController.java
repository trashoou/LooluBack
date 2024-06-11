package loolu.loolu_backend.security.sec_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.dto.LoginRequestDto;
import loolu.loolu_backend.security.sec_dto.RefreshRequestDto;
import loolu.loolu_backend.security.sec_dto.TokenResponseDto;
import loolu.loolu_backend.security.sec_service.AuthService;
import loolu.loolu_backend.services.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authorization controller", description = "Controller for security operations, login/logout, getting new tokens etc")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, UserService userService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
            summary = "Login",
            description = "Logging into the system"
    )
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object of a user logging in") LoginRequestDto loginRequest,
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            if (!user.isEnabled()) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not confirmed");
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

            TokenResponseDto tokenDto = authService.login(loginRequest);
            if (tokenDto == null) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to generate token");
            }

            Cookie cookie = new Cookie("Access-Token", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            removeCookie(response);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get new access token",
            description = "Receiving a new access token through providing an existing refresh token"
    )
    @PostMapping("/access")
    public ResponseEntity<Object> getNewAccessToken(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object of an inbound request that contains a refresh token") RefreshRequestDto request,
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        try {
            TokenResponseDto tokenDto = authService.getAccessToken(request.getRefreshToken());
            Cookie cookie = new Cookie("Access-Token", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Logout",
            description = "Logging out from the system"
    )
    @GetMapping("/logout")
    public void logout(
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        removeCookie(response);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
