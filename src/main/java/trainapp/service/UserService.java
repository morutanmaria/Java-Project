package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.UserResponseDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<UserResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getById(Long id) {
        return repo.findById(id)
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}
