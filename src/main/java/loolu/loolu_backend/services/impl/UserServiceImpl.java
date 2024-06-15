package loolu.loolu_backend.services.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User createUser(@Valid User user) {

        if (StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getEmail()) ||
                StringUtils.isEmpty(user.getFirstName()) ||
                StringUtils.isEmpty(user.getLastName())) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (!user.getPassword().matches(passwordPattern)) {
            throw new IllegalArgumentException("Password must have at least 8 characters, one letter, one digit, and one special character.");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(encoder.encode(userDetails.getPassword()));
            }

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
