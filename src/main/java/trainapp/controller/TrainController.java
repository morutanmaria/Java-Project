package trainapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.dto.TrainRequestDTO;
import trainapp.model.Train;
import trainapp.model.User;
import trainapp.service.TrainService;

import java.util.List;

@Controller
@RequestMapping("/trains")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @PostMapping
    public Train create(@RequestBody TrainRequestDTO dto, User user) {
        return service.createTrain(dto, user);
    }

    @GetMapping
    public List<Train> getAll() {
        return service.getAll();
    }
}