package com.example.trainservice;

import com.example.trainservice.model.*;
import com.example.trainservice.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser
public class TrainControllerIT {

    @Container
    @ServiceConnection(type = JdbcConnectionDetails.class)
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainRepository trainRepository;

    @BeforeEach
    void prepare() {
        trainRepository.deleteAll();
    }

    @Test
    void testGetAllTrains() throws Exception {
        final var savedTrains = trainRepository.saveAll(List.of(createTestTrain()));

        mockMvc.perform(get("/api/trains"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(savedTrains.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value(savedTrains.getFirst().getNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(savedTrains.getFirst().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalCars").value(savedTrains.getFirst().getTotalCars()));
    }

    @Test
    void testGetTrainByNumber_whenTrainExists() throws Exception {
        final var savedTrain = trainRepository.save(createTestTrain());
        final var number = savedTrain.getNumber();

        mockMvc.perform(get("/api/trains/{number}", number))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(number))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(savedTrain.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalCars").value(savedTrain.getTotalCars()));
    }

    @Test
    void testGetAllTrainsBetweenStations() throws Exception {
        final var savedTrain = trainRepository.save(createTestTrain());
        var routes = new ArrayList<>(savedTrain.getRoutes());
        routes.sort(Comparator.comparing(Route::getStopOrder));
        final var startStation = routes.getFirst().getStation().getName();
        final var endStation = routes.getLast().getStation().getName();

        mockMvc.perform(get("/api/trains/search?startStation={startStation}&endStation={endStation}", startStation, endStation))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value(savedTrain.getNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(savedTrain.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalCars").value(savedTrain.getTotalCars()));
    }

    @Test
    void testGetTrainByNumber_whenTrainDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/trains/666"))
                .andExpect(status().isNotFound());
    }

    private Train createTestTrain() {
        var stationA = Station.builder()
                .name("Central Station A")
                .city("City A")
                .country("Country X")
                .build();

        var stationB = Station.builder()
                .name("Central Station B")
                .city("City B")
                .country("Country Y")
                .build();

        var route1 = Route.builder()
                .stopOrder(1)
                .station(stationA)
                .departureTime(LocalDateTime.of(2024, 1, 1, 8, 0))
                .arrivalTime(LocalDateTime.of(2024, 1, 1, 7, 30))
                .build();

        var route2 = Route.builder()
                .stopOrder(2)
                .station(stationB)
                .departureTime(LocalDateTime.of(2024, 1, 1, 12, 0))
                .arrivalTime(LocalDateTime.of(2024, 1, 1, 11, 45))
                .build();

        var car1 = Car.builder()
                .number(1)
                .type(CarType.PLATZKART)
                .totalSeats(54)
                .seats(createSeatsForCar(1, 54, CarType.PLATZKART))
                .build();

        var car2 = Car.builder()
                .number(2)
                .type(CarType.COUPE)
                .totalSeats(36)
                .seats(createSeatsForCar(2, 36, CarType.COUPE))
                .build();

        return Train.builder()
                .number("T123")
                .name("Express")
                .totalCars(2)
                .cars(List.of(car1, car2))
                .routes(List.of(route1, route2))
                .build()
                .withAssociations();
    }

    private List<Seat> createSeatsForCar(int carNumber, int totalSeats, CarType carType) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(Seat.builder()
                    .number(i)
                    .type(determineSeatType(i, carType))
                    .build());
        }
        return seats;
    }

    private SeatType determineSeatType(int seatNumber, CarType carType) {
        if (carType == CarType.PLATZKART) {
            return seatNumber % 2 == 0 ? SeatType.UPPER : SeatType.LOWER;
        }
        return seatNumber % 4 < 2 ? SeatType.LOWER : SeatType.UPPER;
    }
}
