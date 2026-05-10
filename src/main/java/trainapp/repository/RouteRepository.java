package trainapp.repository;

import trainapp.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findBySource(String source);
    List<Route> findBySourceAndDestination(String source, String destination);
}
