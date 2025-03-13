package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bookingservice.model.CarType;

@Data
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private Integer number;
    private CarType type;
    private Integer totalSeats;
}
