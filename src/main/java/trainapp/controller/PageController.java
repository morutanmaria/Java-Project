package trainapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trainapp.dto.RouteDTO;
import trainapp.dto.ScheduleRequestDTO;
import trainapp.dto.TrainRequestDTO;
import trainapp.dto.UserRequestDTO;
import trainapp.dto.BookingRequestDTO;
import trainapp.dto.DelayRequestDTO;
import trainapp.service.*;

import java.time.LocalDateTime;

@Controller
public class PageController {

    private final TrainService trainService;
    private final RouteService routeService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    private final UserService userService;
    private final AuthService authService;

    public PageController(TrainService trainService, RouteService routeService,
                          ScheduleService scheduleService, BookingService bookingService,
                          UserService userService, AuthService authService) {
        this.trainService = trainService;
        this.routeService = routeService;
        this.scheduleService = scheduleService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.authService = authService;
    }

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /** Add isAdmin to every page model so the nav fragment can use th:if="${isAdmin}" */
    private void addCommon(Model model, Authentication auth) {
        model.addAttribute("isAdmin", isAdmin(auth));
    }

    @GetMapping("/")
    public String home() { return "redirect:/trains"; }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        try {
            UserRequestDTO dto = new UserRequestDTO();
            dto.setName(name);
            dto.setEmail(email);
            dto.setPassword(password);
            authService.register(dto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/trains")
    public String trainsPage(Model model, Authentication auth) {
        addCommon(model, auth);
        model.addAttribute("trains", trainService.getAll());
        return "trains";
    }

    @PostMapping("/trains")
    public String addTrain(@RequestParam String name,
                           @RequestParam String trainNumber,
                           @RequestParam int totalSeats,
                           RedirectAttributes ra) {
        try {
            TrainRequestDTO dto = new TrainRequestDTO();
            dto.setName(name);
            dto.setTrainNumber(trainNumber);
            dto.setTotalSeats(totalSeats);
            trainService.create(dto);
            ra.addFlashAttribute("success", "Train added successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trains";
    }

    @PostMapping("/trains/{id}/edit")
    public String editTrain(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String trainNumber,
                            @RequestParam int totalSeats,
                            RedirectAttributes ra) {
        try {
            TrainRequestDTO dto = new TrainRequestDTO();
            dto.setName(name);
            dto.setTrainNumber(trainNumber);
            dto.setTotalSeats(totalSeats);
            trainService.update(id, dto);
            ra.addFlashAttribute("success", "Train updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trains";
    }

    @PostMapping("/trains/{id}/delete")
    public String deleteTrain(@PathVariable Long id, RedirectAttributes ra) {
        try {
            trainService.delete(id);
            ra.addFlashAttribute("success", "Train deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trains";
    }

    @PostMapping("/trains/{id}/delay")
    public String reportDelay(@PathVariable Long id,
                              @RequestParam int delayMinutes,
                              RedirectAttributes ra) {
        try {
            trainService.reportDelay(id, delayMinutes);
            ra.addFlashAttribute("success", "Delay reported. Passengers have been notified.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trains";
    }

    @GetMapping("/routes")
    public String routesPage(Model model, Authentication auth) {
        addCommon(model, auth);
        model.addAttribute("routes", routeService.getAll());
        return "routes";
    }

    @PostMapping("/routes")
    public String addRoute(@RequestParam String source,
                           @RequestParam String destination,
                           @RequestParam int distanceKm,
                           RedirectAttributes ra) {
        try {
            RouteDTO dto = new RouteDTO();
            dto.setSource(source);
            dto.setDestination(destination);
            dto.setDistanceKm(distanceKm);
            routeService.create(dto);
            ra.addFlashAttribute("success", "Route added successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/routes";
    }

    @PostMapping("/routes/{id}/edit")
    public String editRoute(@PathVariable Long id,
                            @RequestParam String source,
                            @RequestParam String destination,
                            @RequestParam int distanceKm,
                            RedirectAttributes ra) {
        try {
            RouteDTO dto = new RouteDTO();
            dto.setSource(source);
            dto.setDestination(destination);
            dto.setDistanceKm(distanceKm);
            routeService.update(id, dto);
            ra.addFlashAttribute("success", "Route updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/routes";
    }

    @PostMapping("/routes/{id}/delete")
    public String deleteRoute(@PathVariable Long id, RedirectAttributes ra) {
        try {
            routeService.delete(id);
            ra.addFlashAttribute("success", "Route deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/routes";
    }

    @GetMapping("/schedules")
    public String schedulesPage(Model model, Authentication auth) {
        addCommon(model, auth);
        model.addAttribute("schedules", scheduleService.getAll());
        model.addAttribute("trains",    trainService.getAll());
        model.addAttribute("routes",    routeService.getAll());
        return "schedules";
    }

    @PostMapping("/schedules")
    public String addSchedule(@RequestParam Long trainId,
                              @RequestParam Long routeId,
                              @RequestParam String departureTime,
                              @RequestParam String arrivalTime,
                              RedirectAttributes ra) {
        try {
            ScheduleRequestDTO dto = new ScheduleRequestDTO();
            dto.setTrainId(trainId);
            dto.setRouteId(routeId);
            dto.setDepartureTime(LocalDateTime.parse(departureTime));
            dto.setArrivalTime(LocalDateTime.parse(arrivalTime));
            scheduleService.create(dto);
            ra.addFlashAttribute("success", "Schedule added successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/schedules";
    }

    @PostMapping("/schedules/{id}/edit")
    public String editSchedule(@PathVariable Long id,
                               @RequestParam Long trainId,
                               @RequestParam Long routeId,
                               @RequestParam String departureTime,
                               @RequestParam String arrivalTime,
                               RedirectAttributes ra) {
        try {
            ScheduleRequestDTO dto = new ScheduleRequestDTO();
            dto.setTrainId(trainId);
            dto.setRouteId(routeId);
            dto.setDepartureTime(LocalDateTime.parse(departureTime));
            dto.setArrivalTime(LocalDateTime.parse(arrivalTime));
            scheduleService.update(id, dto);
            ra.addFlashAttribute("success", "Schedule updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/schedules";
    }

    @PostMapping("/schedules/{id}/delete")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes ra) {
        try {
            scheduleService.delete(id);
            ra.addFlashAttribute("success", "Schedule deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/schedules";
    }

    @GetMapping("/journey")
    public String journeyPage(@RequestParam(required = false) String from,
                              @RequestParam(required = false) String to,
                              Model model, Authentication auth) {
        addCommon(model, auth);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        if (from != null && to != null && !from.isBlank() && !to.isBlank()) {
            var results = scheduleService.findJourneys(from, to);
            if (results.isEmpty()) {
                model.addAttribute("noResults", true);
            } else {
                model.addAttribute("results", results);
            }
        }
        return "journey";
    }

    @GetMapping("/bookings")
    public String bookingsPage(@RequestParam(required = false) Long trainId,
                               Model model, Authentication auth) {
        addCommon(model, auth);
        boolean admin = isAdmin(auth);
        model.addAttribute("isAdmin", admin);
        if (admin) {
            model.addAttribute("trains", trainService.getAll());
            model.addAttribute("selectedTrainId", trainId);
            if (trainId != null) {
                model.addAttribute("bookings", bookingService.getBookingsByTrain(trainId));
            } else {
                model.addAttribute("bookings", bookingService.getAllBookings());
            }
        } else {
            model.addAttribute("bookings", bookingService.getMyBookings(auth.getName()));
        }
        return "bookings";
    }

    @GetMapping("/bookings/new")
    public String newBookingPage(@RequestParam(required = false) Long scheduleId,
                                 Model model, Authentication auth) {
        addCommon(model, auth);
        model.addAttribute("schedules", scheduleService.getAll());
        model.addAttribute("preselectedScheduleId", scheduleId);
        return "book";
    }

    @PostMapping("/bookings")
    public String createBooking(@RequestParam Long scheduleId,
                                @RequestParam int seatsBooked,
                                Authentication auth,
                                RedirectAttributes ra,
                                Model model) {
        try {
            BookingRequestDTO dto = new BookingRequestDTO();
            dto.setScheduleId(scheduleId);
            dto.setSeatsBooked(seatsBooked);
            bookingService.createBooking(auth.getName(), dto);
            ra.addFlashAttribute("success", "Booking confirmed! A confirmation email has been sent.");
            return "redirect:/bookings";
        } catch (Exception e) {
            addCommon(model, auth);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("schedules", scheduleService.getAll());
            model.addAttribute("preselectedScheduleId", scheduleId);
            return "book";
        }
    }

    @GetMapping("/users")
    public String usersPage(Model model, Authentication auth) {
        addCommon(model, auth);
        var users = userService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("adminCount", users.stream().filter(u -> "ADMIN".equals(u.getRole())).count());
        model.addAttribute("userCount",  users.stream().filter(u -> "USER".equals(u.getRole())).count());
        return "users";
    }
}