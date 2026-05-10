package trainapp.dto;

import trainapp.model.Schedule;
import java.util.List;

public class JourneyResultDTO {
    private String type; // DIRECT or CHANGEOVER
    private List<Schedule> legs;
    private String message;

    public JourneyResultDTO(String type, List<Schedule> legs) {
        this.type = type; this.legs = legs;
    }
    public JourneyResultDTO(String message) {
        this.message = message;
    }
    public String getType() { return type; }
    public List<Schedule> getLegs() { return legs; }
    public String getMessage() { return message; }
}
