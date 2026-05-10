package trainapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trainapp.dto.BookingRequestDTO;
import trainapp.exception.OverbookingException;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.Booking;
import trainapp.model.Schedule;
import trainapp.model.User;
import trainapp.repository.BookingRepository;
import trainapp.repository.ScheduleRepository;
import trainapp.repository.UserRepository;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ScheduleRepository scheduleRepo;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepo, UserRepository userRepo,
                          ScheduleRepository scheduleRepo, EmailService emailService) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.scheduleRepo = scheduleRepo;
        this.emailService = emailService;
    }

    @Transactional
    public Booking createBooking(String userEmail, BookingRequestDTO dto) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Schedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + dto.getScheduleId()));

        if (schedule.getAvailableSeats() < dto.getSeatsBooked()) {
            throw new OverbookingException(
                "Not enough seats. Requested: " + dto.getSeatsBooked() +
                ", Available: " + schedule.getAvailableSeats());
        }

        schedule.setAvailableSeats(schedule.getAvailableSeats() - dto.getSeatsBooked());
        scheduleRepo.save(schedule);

        Booking booking = new Booking(user, schedule, dto.getSeatsBooked());
        Booking saved = bookingRepo.save(booking);

        emailService.sendBookingConfirmation(saved);

        return saved;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> getBookingsByTrain(Long trainId) {
        return bookingRepo.findByTrainId(trainId);
    }

    public List<Booking> getMyBookings(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return bookingRepo.findByUserId(user.getId());
    }
}
