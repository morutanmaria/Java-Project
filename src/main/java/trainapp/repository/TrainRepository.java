package trainapp.repository;

import trainapp.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {
    boolean existsByTrainNumber(String trainNumber);
}
