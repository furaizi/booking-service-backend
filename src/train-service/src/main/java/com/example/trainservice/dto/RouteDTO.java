package com.example.trainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private Long id;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private StationDTO station;
    private Integer stopOrder;
}
