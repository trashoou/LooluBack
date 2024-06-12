package loolu.loolu_backend.security.sec_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private String email;
    private String id;



    public UserProfileDto(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }
}
