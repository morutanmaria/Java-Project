package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.UserRequestDTO;
import trainapp.dto.UserResponseDTO;
import trainapp.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserRequestDTO dto) {
        return service.register(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.getAll().stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }
}