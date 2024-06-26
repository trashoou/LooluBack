package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import loolu.loolu_backend.domain.Role;
import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.dto.UserDto;
import loolu.loolu_backend.dto.UserRegistrationDTO;
import loolu.loolu_backend.repositories.RoleRepository;
import loolu.loolu_backend.services.impl.EmailService;
import loolu.loolu_backend.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "Controller for managing users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private EmailService emailService;


    @Autowired
    private UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        User createdUser = userService.createUser(userRegistrationDTO);

//        String subject = "Welcome to our Loolu service!";
//        String text = "Greetings, " + userRegistrationDTO.getFirstName() +
//                "! Thank you for signing up for our Loolu service.";
//        emailService.sendEmail(userRegistrationDTO.getEmail(), subject, text);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDto userDetails) {
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (userDetails.getFirstName() != null) {
            existingUser.setFirstName(userDetails.getFirstName());
        }
        if (userDetails.getLastName() != null) {
            existingUser.setLastName(userDetails.getLastName());
        }
        if (userDetails.getEmail() != null) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(userDetails.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        if (userDetails.getUsername() != null) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getAvatarPath() != null) {
            existingUser.setAvatarPath(userDetails.getAvatarPath());
        }

        User updatedUser = userService.updateUser(id, existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}