package loolu.loolu_backend.security.sec_service;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.security.auth.message.AuthException;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.dto.LoginRequestDto;
import loolu.loolu_backend.security.sec_dto.AuthInfo;
import loolu.loolu_backend.security.sec_dto.TokenResponseDto;
import loolu.loolu_backend.security.sec_dto.UserProfileDto;
import loolu.loolu_backend.services.impl.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserService userService, TokenService tokenService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.encoder = encoder;
        this.refreshStorage = new HashMap<>();
    }

    public TokenResponseDto login(@Nonnull LoginRequestDto loginRequest) throws AuthException {
        String email = loginRequest.getEmail();
        User foundUser = userService.findByEmail(email);

        if (foundUser != null && encoder.matches(loginRequest.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(email, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Invalid email or password");
        }
    }

    public TokenResponseDto getAccessToken(@Nonnull String refreshToken) {
        if (tokenService.validateRefreshToken(refreshToken)) {
            Claims refreshClaims = tokenService.getRefreshClaims(refreshToken);
            String email = refreshClaims.getSubject();
            String savedRefreshToken = refreshStorage.get(email);

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.findByEmail(email);
                String accessToken = tokenService.generateAccessToken(user);
                return new TokenResponseDto(accessToken, null);
            }
        }
        return new TokenResponseDto(null, null);
    }

    public UserProfileDto getUserProfile(String token) {
        if (tokenService.validateAccessToken(token)) {
            Claims claims = tokenService.getAccessClaims(token);
            String email = claims.getSubject();
            User user = userService.findByEmail(email);

            if (user != null) {
                return new UserProfileDto(
                        String.valueOf(user.getId()),
                        user.getUsername()
                );
            }
        }
        return null; // или выбросить исключение, если токен недействителен или пользователь не найден
    }

    public AuthInfo getAuthInfo() {
        return (AuthInfo) SecurityContextHolder.getContext().getAuthentication();
    }
}


