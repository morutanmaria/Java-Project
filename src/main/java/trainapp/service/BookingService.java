package trainapp.service;

import org.springframework.stereotype.Service;
import trainapp.dto.BookingRequestDTO;
import trainapp.exception.ResourceNotFoundException;
import trainapp.model.*;
import trainapp.repository.*;
import trainapp.security.*;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ScheduleRepository scheduleRepo;

    public BookingService(BookingRepository bookingRepo,
                          UserRepository userRepo,
                          ScheduleRepository scheduleRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.scheduleRepo = scheduleRepo;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public Booking createBooking(BookingRequestDTO dto) {

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Schedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.getAvailableSeats() < dto.getSeatsBooked()) {
            throw new RuntimeException("Not enough seats");
        }

        schedule.setAvailableSeats(
                schedule.getAvailableSeats() - dto.getSeatsBooked()
        );

        scheduleRepo.save(schedule);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setSeatsBooked(dto.getSeatsBooked());
        booking.setBookingStatus("CONFIRMED");

        return bookingRepo.save(booking);
    }
}