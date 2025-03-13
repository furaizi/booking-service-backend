package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainDTO {
    private Long id;
    private String number;
    private String name;
    private Integer totalCars;
}
