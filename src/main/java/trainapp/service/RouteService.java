package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.model.Route;
import trainapp.model.User;
import trainapp.repository.RouteRepository;
import trainapp.security.RoleChecker;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository repo;

    public RouteService(RouteRepository repo) {
        this.repo = repo;
    }

    public Route addRoute(Route route) {
        return repo.save(route);
    }

    public List<Route> getAllRoutes() {
        return repo.findAll();
    }
    public Route addRoute(Route route, User user) {

        RoleChecker.requireAdmin(user);

        return repo.save(route);
    }
}