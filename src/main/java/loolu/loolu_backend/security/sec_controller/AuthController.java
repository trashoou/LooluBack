package loolu.loolu_backend.security.sec_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.security.sec_dto.RefreshRequestDto;
import loolu.loolu_backend.security.sec_dto.TokenResponseDto;
import loolu.loolu_backend.security.sec_service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authorization controller", description = "Controller for security operations, login/logout, getting new tokens etc")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            summary = "Login",
            description = "Logging into the system"
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object of an user that logging in") User user,
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        try {
            TokenResponseDto tokenDto = service.login(user);

            Cookie cookie = new Cookie("Access-Token", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return ResponseEntity.ok(tokenDto);
        } catch (AuthException e) {
            TokenResponseDto tokenDto = new TokenResponseDto(e.getMessage());
            return new ResponseEntity<>(tokenDto, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get new access token",
            description = "Receiving a new access token through providing an existing refresh token"
    )
    @PostMapping("/access")
    public ResponseEntity<TokenResponseDto> getNewAccessToken(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object of an inbound request that contains a refresh token") RefreshRequestDto request
    ) {
        TokenResponseDto accessToken = service.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(accessToken);
    }

    @Operation(
            summary = "Logout",
            description = "Logging out from the system"
    )
    @GetMapping("/logout")
    public void logout(
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
//    @Operation(
//            summary = "Delete User",
//            description = "Delete user by ID"
//    )
//    @DeleteMapping("/user/{id}")
//    public ResponseEntity<String> deleteUser(
//            @Parameter(description = "ID of the user to be deleted") @PathVariable("id") Long id
//    ) {
//        try {
//            service.deleteUser(id);
//            return ResponseEntity.ok("User deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
//        }
//    }
}