package trainapp.dto;

public class TrainResponseDTO {
    private Long id;
    private String name;
    private String trainNumber;
    private int totalSeats;

    public TrainResponseDTO() {}

    public TrainResponseDTO(Long id, String name, String trainNumber, int totalSeats) {
        this.id = id;
        this.name = name;
        this.trainNumber = trainNumber;
        this.totalSeats = totalSeats;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    public int getTotalSeats() {
        return totalSeats;
    }
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }


}