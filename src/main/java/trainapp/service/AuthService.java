package trainapp.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import trainapp.dto.AuthRequestDTO;
import trainapp.dto.AuthResponseDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.User;
import trainapp.repository.UserRepository;
import trainapp.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {

        User user = repo.findByEmail(dto.getEmail());

        if (user == null || !encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                "TEMP_TOKEN"
        );
    }
}