package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.BookingRequestDTO;
import trainapp.model.Booking;
import trainapp.service.BookingService;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public Booking create(@RequestBody BookingRequestDTO dto) {
        return service.createBooking(dto);
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAllBookings();
    }
}