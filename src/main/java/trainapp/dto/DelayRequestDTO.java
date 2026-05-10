package trainapp.dto;

import jakarta.validation.constraints.Min;

public class DelayRequestDTO {
    @Min(1) private int delayMinutes;

    public DelayRequestDTO() {}
    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }
}
