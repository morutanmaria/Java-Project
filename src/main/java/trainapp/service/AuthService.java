package trainapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trainapp.dto.AuthRequestDTO;
import trainapp.dto.AuthResponseDTO;
import trainapp.dto.UserRequestDTO;
import trainapp.dto.UserResponseDTO;
import trainapp.model.Role;
import trainapp.model.User;
import trainapp.repository.UserRepository;
import trainapp.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDTO register(UserRequestDTO dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")
                ? Role.ADMIN : Role.USER);
        User saved = repo.save(user);
        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole(), saved.getCreatedAt());
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponseDTO(user.getId(), user.getEmail(), user.getRole(), token);
    }
}
