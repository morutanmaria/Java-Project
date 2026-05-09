package trainapp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import trainapp.dto.TrainRequestDTO;
import trainapp.model.User;
import trainapp.repository.TrainRepository;
import trainapp.model.Train;
import trainapp.security.RoleChecker;


@Service
public class TrainService {

    private final TrainRepository repo;

    public TrainService(TrainRepository repo) {
        this.repo = repo;
    }

    public Train addTrain(Train train) {
        return repo.save(train);
    }

    public List<Train> getAll() {
        return repo.findAll();
    }
    public Train createTrain(TrainRequestDTO dto,  User user) {

        RoleChecker.requireAdmin(user);
        Train train = new Train();
        train.setName(dto.getName());
        train.setTrainNumber(dto.getTrainNumber());
        train.setTotalSeats(dto.getTotalSeats());

        return repo.save(train);
    }
}