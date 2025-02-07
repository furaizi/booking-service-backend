package com.example.trainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Long id;
    private Long carId;
    private int number;
    private SeatType type;
}
