package com.example.trainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainDTO {
    private Long id;
    private String number;
    private String name;
    private List<CarDTO> cars;
    private List<RouteDTO> routes;
}
