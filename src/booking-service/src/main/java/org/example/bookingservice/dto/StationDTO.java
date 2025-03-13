package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StationDTO {
    private Long id;
    private String name;
    private String city;
}
