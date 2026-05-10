package trainapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import trainapp.model.Booking;
import trainapp.model.Train;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final boolean mock;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.mock:true}") boolean mock) {
        this.mailSender = mailSender;
        this.mock = mock;
    }

    public void sendBookingConfirmation(Booking booking) {
        String to = booking.getUser().getEmail();
        String subject = "Booking Confirmed – Ticket #" + booking.getId();
        String body = String.format(
            "Dear %s,\n\n" +
            "Your booking has been CONFIRMED.\n\n" +
            "Booking ID   : %d\n" +
            "Train        : %s (%s)\n" +
            "Route        : %s → %s\n" +
            "Departure    : %s\n" +
            "Arrival      : %s\n" +
            "Seats booked : %d\n" +
            "Status       : %s\n\n" +
            "Have a great journey!\n" +
            "Train Ticketing App",
            booking.getUser().getName(),
            booking.getId(),
            booking.getSchedule().getTrain().getName(),
            booking.getSchedule().getTrain().getTrainNumber(),
            booking.getSchedule().getRoute().getSource(),
            booking.getSchedule().getRoute().getDestination(),
            booking.getSchedule().getDepartureTime(),
            booking.getSchedule().getArrivalTime(),
            booking.getSeatsBooked(),
            booking.getBookingStatus()
        );
        send(to, subject, body);
    }

    public void sendDelayNotification(String email, String userName, Train train, int delayMinutes) {
        String subject = "⚠ Train Delay Notice – " + train.getName();
        String body = String.format(
            "Dear %s,\n\n" +
            "We regret to inform you that train %s (%s) is delayed by %d minutes.\n\n" +
            "We apologise for the inconvenience.\n" +
            "Train Ticketing App",
            userName, train.getName(), train.getTrainNumber(), delayMinutes
        );
        send(email, subject, body);
    }

    private void send(String to, String subject, String body) {
        if (mock) {
            System.out.println("\n===== [MOCK EMAIL] =====");
            System.out.println("TO      : " + to);
            System.out.println("SUBJECT : " + subject);
            System.out.println("BODY    :\n" + body);
            System.out.println("========================\n");
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }
}
