package loolu.loolu_backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Schema(description = "User entity")
@Entity(name = "DomainUser")
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Schema(description = "Username that use for logging in", example = "Sasha")
    @Column(name = "username")
    private String username;

    @Schema(description = "User's raw password for logging in", example = "111")
    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

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

    @Override
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
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}