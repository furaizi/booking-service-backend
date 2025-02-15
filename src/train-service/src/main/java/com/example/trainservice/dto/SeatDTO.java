package com.example.trainservice.dto;

import com.example.trainservice.model.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDTO {
    private Long id;
    private Integer number;
    private SeatType type;
}
