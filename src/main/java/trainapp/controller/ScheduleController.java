package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.ScheduleRequestDTO;
import trainapp.model.Schedule;
import trainapp.service.ScheduleService;

import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public Schedule create(@RequestBody ScheduleRequestDTO dto) {
        return service.createSchedule(dto);
    }

    @GetMapping
    public List<Schedule> getAll() {
        return service.getAllSchedules();
    }
}
