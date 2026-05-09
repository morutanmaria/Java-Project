package trainapp.dto;

import java.time.LocalDateTime;

public class ScheduleResponseDTO {
    private Long id;
    private Long trainId;
    private Long routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public ScheduleResponseDTO() {}

    public ScheduleResponseDTO(Long id, Long trainId, Long routeId, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.id = id;
        this.trainId = trainId;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTrainId() {
        return trainId;
    }
    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }
    public Long getRouteId() {
        return routeId;
    }
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

}