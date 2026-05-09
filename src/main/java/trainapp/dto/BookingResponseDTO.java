package trainapp.dto;

import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private Long scheduleId;
    private int seatsBooked;
    private String bookingStatus;
    private LocalDateTime bookingTime;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long id, Long userId, Long scheduleId, int seatsBooked, String bookingStatus, LocalDateTime bookingTime) {
        this.id = id;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.seatsBooked = seatsBooked;
        this.bookingStatus = bookingStatus;
        this.bookingTime = bookingTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public String getBookingStatus() {
        return bookingStatus;
    }
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

}