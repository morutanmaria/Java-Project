package trainapp.security;

import trainapp.exception.ResourceNotFoundException;
import trainapp.model.User;

public class RoleChecker {

    public static void requireAdmin(User user) {
        if (!"ADMIN".equals(user.getRole())) {
            throw new ResourceNotFoundException("Access denied: ADMIN only");
        }
    }

    public static void requireUser(User user) {
        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new ResourceNotFoundException("Access denied");
        }
    }
}