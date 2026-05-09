package trainapp.dto;

public class BookingRequestDTO {
    private Long userId;
    private Long scheduleId;
    private int seatsBooked;

    public BookingRequestDTO() {}

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    public int getSeatsBooked() {
        return seatsBooked;
    }
    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

}