package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bookingservice.model.SeatType;

@Data
@AllArgsConstructor
public class SeatDTO {
    private Long id;
    private Integer number;
    private SeatType type;
}
