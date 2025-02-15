package com.example.trainservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Train {
    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String number;
    private String name;
    private int totalCars;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Car> cars;
}
