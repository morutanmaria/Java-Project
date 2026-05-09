package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.model.Route;
import trainapp.service.RouteService;

import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @PostMapping
    public Route addRoute(@RequestBody Route route) {
        return service.addRoute(route);
    }

    @GetMapping
    public List<Route> getAll() {
        return service.getAllRoutes();
    }
}