package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.ScheduleRequestDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.Schedule;
import trainapp.model.Train;
import trainapp.model.Route;
import trainapp.model.User;
import trainapp.repository.ScheduleRepository;
import trainapp.repository.TrainRepository;
import trainapp.repository.RouteRepository;
import trainapp.security.RoleChecker;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepo;
    private final TrainRepository trainRepo;
    private final RouteRepository routeRepo;

    public ScheduleService(ScheduleRepository scheduleRepo,
                           TrainRepository trainRepo,
                           RouteRepository routeRepo) {
        this.scheduleRepo = scheduleRepo;
        this.trainRepo = trainRepo;
        this.routeRepo = routeRepo;
    }

    public Schedule createSchedule(ScheduleRequestDTO dto) {

        Train train = trainRepo.findById(dto.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        Route route = routeRepo.findById(dto.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Schedule schedule = new Schedule();
        schedule.setTrain(train);
        schedule.setRoute(route);
        schedule.setDepartureTime(dto.getDepartureTime());
        schedule.setArrivalTime(dto.getArrivalTime());
        schedule.setAvailableSeats(train.getTotalSeats());

        return scheduleRepo.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }

    public Schedule createSchedule(Schedule schedule, User user) {

        RoleChecker.requireAdmin(user);

        Train train = trainRepo.findById(schedule.getTrain().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Train not found"));

        Route route = routeRepo.findById(schedule.getRoute().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        schedule.setTrain(train);
        schedule.setRoute(route);

        return scheduleRepo.save(schedule);
    }
}