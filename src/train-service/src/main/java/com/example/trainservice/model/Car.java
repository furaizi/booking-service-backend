package com.example.trainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private Long id;
    private Long trainId;
    private int number;
    private CarType type;
    private List<Seat> seats;
    private int totalSeats;
}
