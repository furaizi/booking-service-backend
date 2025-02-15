package com.example.trainservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Route {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private Integer stopOrder;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
}
