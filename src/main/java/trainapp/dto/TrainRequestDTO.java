package trainapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TrainRequestDTO {
    @NotBlank private String name;
    @NotBlank private String trainNumber;
    @Min(1) private int totalSeats;

    public TrainRequestDTO() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
}
