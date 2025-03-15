package org.example.bookingservice;

import org.example.bookingservice.mapper.TicketMapper;
import org.example.bookingservice.model.*;
import org.example.bookingservice.repository.TicketRepository;
import org.example.bookingservice.service.TicketService;
import org.example.bookingservice.service.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketMapper ticketMapper;

    private TicketService ticketService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketMapper = Mappers.getMapper(TicketMapper.class);
        ticketService = new TicketServiceImpl(ticketRepository, ticketMapper);
    }

    @Test
    void testGetAllTickets() {
        var testTicket = createTestTicket();

        when(ticketRepository.findAll()).thenReturn(List.of(testTicket));
        var foundTickets = ticketService.getAllTickets();

        assertNotNull(foundTickets);
        assertEquals(1, foundTickets.size());
        assertEquals(testTicket.getPrice(), foundTickets.getFirst().getPrice());
        assertEquals(testTicket.getStatus(), foundTickets.getFirst().getStatus());
        assertEquals(testTicket.getUser().getPhoneNumber(), foundTickets.getFirst().getUser().getPhoneNumber());
        assertEquals(testTicket.getTrain().getName(), foundTickets.getFirst().getTrain().getName());

        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTicketsOfUser() {
        var testTicket = createTestTicket();
        var userId = testTicket.getUser().getId();

        when(ticketRepository.findAllTicketsByUser_Id(userId)).thenReturn(List.of(testTicket));
        var foundTickets = ticketService.getAllTicketsOfUser(userId);

        assertNotNull(foundTickets);
        assertEquals(1, foundTickets.size());
        assertEquals(testTicket.getPrice(), foundTickets.getFirst().getPrice());
        assertEquals(testTicket.getStatus(), foundTickets.getFirst().getStatus());
        assertEquals(testTicket.getUser().getPhoneNumber(), foundTickets.getFirst().getUser().getPhoneNumber());
        assertEquals(testTicket.getTrain().getName(), foundTickets.getFirst().getTrain().getName());

        verify(ticketRepository, times(1)).findAllTicketsByUser_Id(userId);
    }

    @Test
    void testGetAllTicketsOfNonExistentUser() {
        var userId = 999L;

        when(ticketRepository.findAllTicketsByUser_Id(userId)).thenReturn(List.of());
        var foundTickets = ticketService.getAllTicketsOfUser(userId);
        assertNotNull(foundTickets);
        assertEquals(0, foundTickets.size());

        verify(ticketRepository, times(1)).findAllTicketsByUser_Id(userId);
    }

    @Test
    void getAllTicketsOfTrain() {
        var testTicket = createTestTicket();
        var trainNumber = testTicket.getTrain().getNumber();

        when(ticketRepository.findAllTicketsByTrain_Number(trainNumber)).thenReturn(List.of(testTicket));
        var foundTickets = ticketService.getAllTicketsOfTrain(trainNumber);

        assertNotNull(foundTickets);
        assertEquals(1, foundTickets.size());
        assertEquals(testTicket.getPrice(), foundTickets.getFirst().getPrice());
        assertEquals(testTicket.getStatus(), foundTickets.getFirst().getStatus());
        assertEquals(testTicket.getUser().getPhoneNumber(), foundTickets.getFirst().getUser().getPhoneNumber());
        assertEquals(testTicket.getTrain().getName(), foundTickets.getFirst().getTrain().getName());

        verify(ticketRepository, times(1)).findAllTicketsByTrain_Number(trainNumber);
    }

    @Test
    void getAllTicketsOfNonExistentTrain() {
        var trainNumber = "999";

        when(ticketRepository.findAllTicketsByTrain_Number(trainNumber)).thenReturn(List.of());
        var foundTickets = ticketService.getAllTicketsOfTrain(trainNumber);
        assertNotNull(foundTickets);
        assertEquals(0, foundTickets.size());

        verify(ticketRepository, times(1)).findAllTicketsByTrain_Number(trainNumber);
    }

    private Ticket createTestTicket() {
        // Создаем станции
        Station stationA = Station.builder()
                .name("Station A")
                .city("City A")
                .country("Country A")
                .build();

        Station stationB = Station.builder()
                .name("Station B")
                .city("City B")
                .country("Country B")
                .build();

// Создаем поезд
        Train train = Train.builder()
                .number("123")
                .name("Test Train")
                .totalCars(5)
                .build();

// Создаем маршруты (departureStation и arrivalStation)
        LocalDateTime now = LocalDateTime.now();
        Route route1 = Route.builder()
                .train(train)
                .station(stationA)
                .stopOrder(1)
                .arrivalTime(now)
                .departureTime(now.plusHours(1))
                .build();

        Route route2 = Route.builder()
                .train(train)
                .station(stationB)
                .stopOrder(2)
                .arrivalTime(now.plusHours(2))
                .departureTime(now.plusHours(3))
                .build();

// Создаем вагон
        Car car = Car.builder()
                .train(train)
                .number(1)
                .type(CarType.COUPE)
                .totalSeats(36)
                .build();

// Создаем место
        Seat seat = Seat.builder()
                .car(car)
                .number(1)
                .type(SeatType.LOWER)
                .build();

// Создаем пользователя
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .passwordHash("hashed_password")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(Role.USER)
                .build();

// Создаем тестовый объект Ticket

        return Ticket.builder()
                .user(user)
                .train(train)
                .car(car)
                .seat(seat)
                .departureStation(route1)
                .arrivalStation(route2)
                .price(100.0)
                .status(TicketStatus.BOOKED)
                .build();
    }
}
