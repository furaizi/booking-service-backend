package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RouteDTO {
    private Long id;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private StationDTO station;
    private Integer stopOrder;
}
