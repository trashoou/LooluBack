package loolu.loolu_backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class AuthorityDto implements GrantedAuthority {

    private String authority;

    public AuthorityDto(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}