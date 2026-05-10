package trainapp.repository;

import trainapp.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.route.source = :source AND s.route.destination = :dest")
    List<Schedule> findDirectSchedules(@Param("source") String source, @Param("dest") String dest);

    @Query("SELECT s FROM Schedule s WHERE s.train.id = :trainId")
    List<Schedule> findByTrainId(@Param("trainId") Long trainId);
}
