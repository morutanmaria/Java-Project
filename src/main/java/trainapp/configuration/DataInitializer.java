package trainapp.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import trainapp.model.*;
import trainapp.repository.*;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               TrainRepository trainRepo,
                               RouteRepository routeRepo,
                               ScheduleRepository scheduleRepo,
                               PasswordEncoder encoder) {
        return args -> {
            if (!userRepo.existsByEmail("admin@trainapp.com")) {
                userRepo.save(new User("Admin", "admin@trainapp.com",
                        encoder.encode("admin123"), Role.ADMIN));
            }

            Train t1 = trainRepo.existsByTrainNumber("IC100") ? null :
                    trainRepo.save(new Train("InterCity 100", "IC100", 200));
            Train t2 = trainRepo.existsByTrainNumber("R200") ? null :
                    trainRepo.save(new Train("Regional 200", "R200", 100));
            Train t3 = trainRepo.existsByTrainNumber("EX300") ? null :
                    trainRepo.save(new Train("Express 300", "EX300", 150));

            if (t1 == null) t1 = trainRepo.findAll().stream()
                    .filter(t -> "IC100".equals(t.getTrainNumber())).findFirst().orElse(null);
            if (t2 == null) t2 = trainRepo.findAll().stream()
                    .filter(t -> "R200".equals(t.getTrainNumber())).findFirst().orElse(null);
            if (t3 == null) t3 = trainRepo.findAll().stream()
                    .filter(t -> "EX300".equals(t.getTrainNumber())).findFirst().orElse(null);

            Route r1 = new Route("Cluj-Napoca", "Bucharest", 450);
            Route r2 = new Route("Bucharest", "Constanta", 225);
            Route r3 = new Route("Cluj-Napoca", "Brasov", 150);
            Route r4 = new Route("Brasov", "Bucharest", 165);
            Route r5 = new Route("Bucharest", "Timisoara", 540);

            if (routeRepo.findBySourceAndDestination("Cluj-Napoca", "Bucharest").isEmpty()) {
                r1 = routeRepo.save(r1);
            } else {
                r1 = routeRepo.findBySourceAndDestination("Cluj-Napoca", "Bucharest").get(0);
            }
            if (routeRepo.findBySourceAndDestination("Bucharest", "Constanta").isEmpty()) {
                r2 = routeRepo.save(r2);
            } else {
                r2 = routeRepo.findBySourceAndDestination("Bucharest", "Constanta").get(0);
            }
            if (routeRepo.findBySourceAndDestination("Cluj-Napoca", "Brasov").isEmpty()) {
                r3 = routeRepo.save(r3);
            } else {
                r3 = routeRepo.findBySourceAndDestination("Cluj-Napoca", "Brasov").get(0);
            }
            if (routeRepo.findBySourceAndDestination("Brasov", "Bucharest").isEmpty()) {
                r4 = routeRepo.save(r4);
            } else {
                r4 = routeRepo.findBySourceAndDestination("Brasov", "Bucharest").get(0);
            }
            if (routeRepo.findBySourceAndDestination("Bucharest", "Timisoara").isEmpty()) {
                r5 = routeRepo.save(r5);
            } else {
                r5 = routeRepo.findBySourceAndDestination("Bucharest", "Timisoara").get(0);
            }


            if (scheduleRepo.count() == 0 && t1 != null && t2 != null && t3 != null) {
                LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(6).withMinute(0).withSecond(0).withNano(0);

                scheduleRepo.save(new Schedule(t1, r1, base, base.plusHours(7), t1.getTotalSeats()));
                scheduleRepo.save(new Schedule(t1, r1, base.withHour(14), base.withHour(21), t1.getTotalSeats()));

                scheduleRepo.save(new Schedule(t3, r2, base.withHour(8), base.withHour(11), t3.getTotalSeats()));

                scheduleRepo.save(new Schedule(t2, r3, base.withHour(7), base.withHour(9), t2.getTotalSeats()));
                scheduleRepo.save(new Schedule(t2, r4, base.withHour(10), base.withHour(13), t2.getTotalSeats()));

                scheduleRepo.save(new Schedule(t1, r5, base.withHour(9), base.withHour(18), t1.getTotalSeats()));
            }

            System.out.println("=== Train Ticketing App started ===");
            System.out.println("Admin: admin@trainapp.com / admin123");
            System.out.println("API docs via README.md");
        };
    }
}
