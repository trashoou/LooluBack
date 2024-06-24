package loolu.loolu_backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import loolu.loolu_backend.models.Cart;
import loolu.loolu_backend.models.UserOrder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Schema(description = "User entity")
@Entity(name = "DomainUser")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Schema(description = "User's first name", example = "Sasha")
    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Schema(description = "User's last name", example = "Ivanyo")
    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Schema(description = "User's email address", example = "sasha@example.com")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Schema(description = "User's raw password for logging in", example = "111") //Password1!
    @NotBlank(message = "Password is mandatory")
    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;

    @Setter
    @Schema(description = "User's username or nickname for logging in", example = "Sancos")
    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", unique = true, nullable = true)
    private String username;

    @Schema(description = "Path to user's avatar", example = "/avatar/user1.png")
    @Column(name = "avatar_path", nullable = true)
    private String avatarPath;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(
            description = "List of roles granted to user",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart carts;

    @Schema(
            description = "List of authorities granted to user",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Schema(description = "User's username for logging in", example = "sasha.ivanov")
    public String getUsername() {
        return username;
    }

    @Schema(
            description = "True if user's account is not expired",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(
            description = "True if user's account is not locked",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(
            description = "True if user's credentials is not expired",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(description = "True if user is enabled",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username) &&
                Objects.equals(avatarPath, user.avatarPath);
        // Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, username); //roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                //", roles=" + roles +
                '}';
    }
}