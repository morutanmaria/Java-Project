package trainapp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.DelayRequestDTO;
import trainapp.dto.TrainRequestDTO;
import trainapp.model.Train;
import trainapp.service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Train>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Train> create(@Valid @RequestBody TrainRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Train> update(@PathVariable Long id, @Valid @RequestBody TrainRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/delay")
    public ResponseEntity<Train> reportDelay(@PathVariable Long id, @Valid @RequestBody DelayRequestDTO dto) {
        return ResponseEntity.ok(service.reportDelay(id, dto.getDelayMinutes()));
    }
}
