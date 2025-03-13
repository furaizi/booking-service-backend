package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bookingservice.model.TicketStatus;

@Data
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private UserDTO user;
    private TrainDTO train;
    private CarDTO car;
    private SeatDTO seat;
    private RouteDTO departureStation;
    private RouteDTO arrivalStation;
    private Double price;
    private TicketStatus status;
}
