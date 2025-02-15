package com.example.trainservice.dto;

import com.example.trainservice.model.CarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDTO {
    private Long id;
    private Integer number;
    private CarType type;
    private Integer totalSeats;
    private List<SeatDTO> seats;
}
