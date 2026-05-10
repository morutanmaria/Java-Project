package trainapp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.JourneyResultDTO;
import trainapp.dto.ScheduleRequestDTO;
import trainapp.model.Schedule;
import trainapp.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Schedule> create(@Valid @RequestBody ScheduleRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find journeys (direct or with changeover) between two stations.
     * GET /api/schedules/journey?from=Cluj-Napoca&to=Constanta
     */
    @GetMapping("/journey")
    public ResponseEntity<?> findJourney(@RequestParam String from, @RequestParam String to) {
        List<JourneyResultDTO> results = service.findJourneys(from, to);
        if (results.isEmpty()) {
            return ResponseEntity.ok(
                List.of(new JourneyResultDTO(
                    "No routes found between '" + from + "' and '" + to + "'. " +
                    "No direct or changeover connections available.")));
        }
        return ResponseEntity.ok(results);
    }
}
