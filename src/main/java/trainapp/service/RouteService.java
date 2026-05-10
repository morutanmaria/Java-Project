package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.RouteDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.Route;
import trainapp.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository repo;

    public RouteService(RouteRepository repo) {
        this.repo = repo;
    }

    public Route create(RouteDTO dto) {
        Route r = new Route(dto.getSource(), dto.getDestination(), dto.getDistanceKm());
        return repo.save(r);
    }

    public List<Route> getAll() {
        return repo.findAll();
    }

    public Route getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    public Route update(Long id, RouteDTO dto) {
        Route r = getById(id);
        r.setSource(dto.getSource());
        r.setDestination(dto.getDestination());
        r.setDistanceKm(dto.getDistanceKm());
        return repo.save(r);
    }

    public void delete(Long id) {
        Route r = getById(id);
        repo.delete(r);
    }
}
