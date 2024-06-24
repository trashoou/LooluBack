package loolu.loolu_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import loolu.loolu_backend.domain.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must have at least 8 characters, one letter, one digit, and one special character.")
    private String password;

    @NotBlank(message = "Username is mandatory")
    private String username;

    private String avatarPath;

    private Long cartId;


}
