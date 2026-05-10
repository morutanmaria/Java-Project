package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.TrainRequestDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.Train;
import trainapp.repository.BookingRepository;
import trainapp.repository.ScheduleRepository;
import trainapp.repository.TrainRepository;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository repo;
    private final ScheduleRepository scheduleRepo;
    private final BookingRepository bookingRepo;
    private final EmailService emailService;

    public TrainService(TrainRepository repo, ScheduleRepository scheduleRepo,
                        BookingRepository bookingRepo, EmailService emailService) {
        this.repo = repo;
        this.scheduleRepo = scheduleRepo;
        this.bookingRepo = bookingRepo;
        this.emailService = emailService;
    }

    public Train create(TrainRequestDTO dto) {
        if (repo.existsByTrainNumber(dto.getTrainNumber())) {
            throw new IllegalArgumentException("Train number already exists: " + dto.getTrainNumber());
        }
        Train t = new Train(dto.getName(), dto.getTrainNumber(), dto.getTotalSeats());
        return repo.save(t);
    }

    public List<Train> getAll() {
        return repo.findAll();
    }

    public Train getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found: " + id));
    }

    public Train update(Long id, TrainRequestDTO dto) {
        Train t = getById(id);
        t.setName(dto.getName());
        t.setTrainNumber(dto.getTrainNumber());
        t.setTotalSeats(dto.getTotalSeats());
        return repo.save(t);
    }

    public void delete(Long id) {
        Train t = getById(id);
        repo.delete(t);
    }

    public Train reportDelay(Long trainId, int delayMinutes) {
        Train train = getById(trainId);
        train.setDelayed(true);
        train.setDelayMinutes(delayMinutes);
        repo.save(train);

        scheduleRepo.findByTrainId(trainId).forEach(schedule -> {
            bookingRepo.findByTrainId(trainId).stream()
                    .filter(b -> b.getSchedule().getTrain().getId().equals(trainId))
                    .map(b -> b.getUser())
                    .distinct()
                    .forEach(user -> emailService.sendDelayNotification(
                            user.getEmail(), user.getName(), train, delayMinutes));
        });

        return train;
    }
}
