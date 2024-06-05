package loolu.loolu_backend.services.impl;

import lombok.RequiredArgsConstructor;
import loolu.loolu_backend.dto.NewUserDto;
import loolu.loolu_backend.dto.UserDto;
import loolu.loolu_backend.exeptions.RestException;
import loolu.loolu_backend.models.User;
import loolu.loolu_backend.repositories.UsersRepository;
import loolu.loolu_backend.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static loolu.loolu_backend.dto.UserDto.from;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UsersService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDto registerUser(NewUserDto newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email <" + newUser.getEmail() + "> already exists");
        }

        User user = User.builder()
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                //.password(newUser.getPassword())
                .role(User.Role.USER)
                .build();
        userRepository.save(user);
        return  from(user);
    }
}