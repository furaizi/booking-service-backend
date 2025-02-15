package com.example.trainservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private Train train;
    private int number;
    private CarType type;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Seat> seats;
    private int totalSeats;
}
