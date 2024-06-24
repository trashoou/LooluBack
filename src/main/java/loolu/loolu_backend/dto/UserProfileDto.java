package loolu.loolu_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import loolu.loolu_backend.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Исключаем null-значения из JSON
public class UserProfileDto implements UserDetails {

    @Schema(description = "User's ID")
    private Integer id;

    @Schema(description = "User's first name")
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Schema(description = "User's last name")
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Schema(description = "User's email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Schema(description = "User's raw password for logging in")
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;

    @Schema(description = "User's username or nickname for logging in")
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Schema(description = "Path to user's avatar")
    private String avatarPath;

    @Schema(description = "List of roles granted to user")
    private Set<Role> roles = new HashSet<>();

    private Set<AuthorityDto> authorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    private Long cartId;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
