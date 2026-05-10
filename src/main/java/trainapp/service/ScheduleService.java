package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.JourneyResultDTO;
import trainapp.dto.ScheduleRequestDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.Route;
import trainapp.model.Schedule;
import trainapp.model.Train;
import trainapp.repository.RouteRepository;
import trainapp.repository.ScheduleRepository;
import trainapp.repository.TrainRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepo;
    private final TrainRepository trainRepo;
    private final RouteRepository routeRepo;

    public ScheduleService(ScheduleRepository scheduleRepo, TrainRepository trainRepo,
                           RouteRepository routeRepo) {
        this.scheduleRepo = scheduleRepo;
        this.trainRepo = trainRepo;
        this.routeRepo = routeRepo;
    }

    public Schedule create(ScheduleRequestDTO dto) {
        Train train = trainRepo.findById(dto.getTrainId())
                .orElseThrow(() -> new ResourceNotFoundException("Train not found"));
        Route route = routeRepo.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        Schedule s = new Schedule(train, route, dto.getDepartureTime(), dto.getArrivalTime(), train.getTotalSeats());
        return scheduleRepo.save(s);
    }

    public List<Schedule> getAll() {
        return scheduleRepo.findAll();
    }

    public Schedule getById(Long id) {
        return scheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + id));
    }

    public Schedule update(Long id, ScheduleRequestDTO dto) {
        Schedule s = getById(id);
        Train train = trainRepo.findById(dto.getTrainId())
                .orElseThrow(() -> new ResourceNotFoundException("Train not found"));
        Route route = routeRepo.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        s.setTrain(train);
        s.setRoute(route);
        s.setDepartureTime(dto.getDepartureTime());
        s.setArrivalTime(dto.getArrivalTime());
        return scheduleRepo.save(s);
    }

    public void delete(Long id) {
        Schedule s = getById(id);
        scheduleRepo.delete(s);
    }

    /**
     * Find possible journeys between two stations.
     * Returns direct schedules and/or changeover options (2-leg journeys).
     */
    public List<JourneyResultDTO> findJourneys(String from, String to) {
        List<JourneyResultDTO> results = new ArrayList<>();

        // 1. Direct routes
        List<Schedule> direct = scheduleRepo.findDirectSchedules(from, to);
        for (Schedule s : direct) {
            results.add(new JourneyResultDTO("DIRECT", List.of(s)));
        }

        // 2. Changeover routes: find all schedules leaving `from`, then check if
        //    any schedule departs from that leg's destination toward `to`
        List<Schedule> allSchedules = scheduleRepo.findAll();
        List<Schedule> firstLegs = allSchedules.stream()
                .filter(s -> s.getRoute().getSource().equalsIgnoreCase(from))
                .filter(s -> !s.getRoute().getDestination().equalsIgnoreCase(to))
                .toList();

        for (Schedule leg1 : firstLegs) {
            String midpoint = leg1.getRoute().getDestination();
            List<Schedule> secondLegs = allSchedules.stream()
                    .filter(s -> s.getRoute().getSource().equalsIgnoreCase(midpoint))
                    .filter(s -> s.getRoute().getDestination().equalsIgnoreCase(to))
                    // Must depart after leg1 arrives (with at least 30 min connection)
                    .filter(s -> s.getDepartureTime().isAfter(leg1.getArrivalTime().plusMinutes(30)))
                    .toList();
            for (Schedule leg2 : secondLegs) {
                results.add(new JourneyResultDTO("CHANGEOVER", List.of(leg1, leg2)));
            }
        }

        return results;
    }
}
