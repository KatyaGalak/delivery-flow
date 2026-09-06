package ru.ekaterina.gateway.service;

import ru.ekaterina.gateway.dto.UserDTO;
import ru.ekaterina.gateway.kafka.producer.Producer;
import ru.ekaterina.gateway.kafka.event.UserRegisteredEvent;
import ru.ekaterina.gateway.model.User;
import ru.ekaterina.gateway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Producer producer;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, Producer producer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.producer = producer;
    }

    public UserDTO registerNewUser(String username, String password, String name, String email, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String userRole = (role != null && !role.isBlank()) ? role : "ROLE_CUSTOMER";

        User user = new User(username, encodedPassword, name, email, Set.of(userRole));
        User savedUser = userRepository.save(user);

        producer.sendUserRegisteredEvent(
                new UserRegisteredEvent(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail())
        );

        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail(), savedUser.getRoles());
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRoles());
    }
}