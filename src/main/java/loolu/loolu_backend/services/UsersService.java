package loolu.loolu_backend.services;

import loolu.loolu_backend.dto.NewUserDto;
import loolu.loolu_backend.dto.UserDto;

public interface UsersService {
    UserDto registerUser(NewUserDto newUser);
}
