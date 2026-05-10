package trainapp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.BookingRequestDTO;
import trainapp.model.Booking;
import trainapp.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /** Book one or multiple seats on a schedule. Authenticated users only. */
    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequestDTO dto,
                                          Authentication auth) {
        Booking booking = service.createBooking(auth.getName(), dto);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /** Get current user's own bookings. */
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> myBookings(Authentication auth) {
        return ResponseEntity.ok(service.getMyBookings(auth.getName()));
    }

    /** Admin: get all bookings. */
    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(service.getAllBookings());
    }

    /** Admin: get all bookings for a specific train. */
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<Booking>> getByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(service.getBookingsByTrain(trainId));
    }
}
