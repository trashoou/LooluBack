package loolu.loolu_backend.services.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import loolu.loolu_backend.domain.Role;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.dto.UserRegistrationDTO;
import loolu.loolu_backend.models.Cart;
import loolu.loolu_backend.repositories.CartRepository;
import loolu.loolu_backend.repositories.RoleRepository;
import loolu.loolu_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           CartRepository cartRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User createUser(UserRegistrationDTO userRegistrationDTO) {

        if (StringUtils.isEmpty(userRegistrationDTO.getUsername()) ||
                StringUtils.isEmpty(userRegistrationDTO.getPassword()) ||
                StringUtils.isEmpty(userRegistrationDTO.getEmail()) ||
                StringUtils.isEmpty(userRegistrationDTO.getFirstName()) ||
                StringUtils.isEmpty(userRegistrationDTO.getLastName())) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (!userRegistrationDTO.getPassword().matches(passwordPattern)) {
            throw new IllegalArgumentException("Password must have at least 8 characters, one letter, one digit, and one special character.");
        }

        User newUser = User.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .email(userRegistrationDTO.getEmail())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .username(userRegistrationDTO.getUsername())
                .avatarPath(userRegistrationDTO.getAvatarPath())
                .build();

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        newUser.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(newUser);

        Cart newCart = new Cart();
        newCart.setUser(savedUser);
        cartRepository.save(newCart);

        return savedUser;
    }

    @Transactional
    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());
            user.setAvatarPath(userDetails.getAvatarPath());

            // Обновление ролей пользователя
            Set<Role> userRoles = new HashSet<>();
            for (Role role : userDetails.getRoles()) {
                Role existingRole = roleRepository.findByName(role.getName());
                if (existingRole != null) {
                    userRoles.add(existingRole);
                } else {
                    userRoles.add(role); // Если роль новая
                }
            }
            user.setRoles(userRoles);

            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
