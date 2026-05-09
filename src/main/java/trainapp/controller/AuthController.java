package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.UserRequestDTO;
import trainapp.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // SHOW REGISTER PAGE
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserRequestDTO());
        return "register";
    }

    // HANDLE REGISTER
    @PostMapping("/register")
    public String register(@ModelAttribute UserRequestDTO dto) {
        userService.register(dto);
        return "redirect:/login";
    }

    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // HANDLE LOGIN (simple version)
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        boolean success = userService.login(email, password);

        if (!success) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        return "redirect:/users";
    }
}