package trainapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import trainapp.dto.UserRequestDTO;
import trainapp.dto.UserResponseDTO;
import trainapp.model.Role;
import trainapp.model.User;
import trainapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;


    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public UserResponseDTO register(UserRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("USER");

        User saved = repo.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public boolean login(String email, String password) {

        User user = repo.findByEmail(email);

        if (user == null) return false;

        return encoder.matches(password, user.getPassword());
    }
}