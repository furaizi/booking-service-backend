package org.example.bookingservice;

import org.example.bookingservice.dto.TicketDTO;
import org.example.bookingservice.mapper.TicketMapper;
import org.example.bookingservice.model.*;
import org.example.bookingservice.repository.TicketRepository;
import org.example.bookingservice.service.TicketService;
import org.example.bookingservice.service.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketMapper ticketMapper;

    private TicketService ticketService;


    @BeforeEach
    void setUp() {
        ticketMapper = Mappers.getMapper(TicketMapper.class);
        ticketService = new TicketServiceImpl(ticketRepository, ticketMapper);
    }

    @Test
    void shouldReturnAllTickets() {
        final var expectedTicket = createTestTicket();
        when(ticketRepository.findAll()).thenReturn(List.of(expectedTicket));

        var actualTickets = ticketService.getAllTickets();

        assertNotNull(actualTickets, "The returned ticket list should not be null");
        assertEquals(1, actualTickets.size(), "Ticket list size should be 1");
        assertTicketEquals(expectedTicket, actualTickets.getFirst());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTicketsForUser() {
        final var expectedTicket = createTestTicket();
        final var userId = expectedTicket.getUser().getId();
        when(ticketRepository.findAllTicketsByUser_Id(userId)).thenReturn(List.of(expectedTicket));

        var actualTickets = ticketService.getAllTicketsOfUser(userId);

        assertNotNull(actualTickets, "The returned ticket list should not be null");
        assertEquals(1, actualTickets.size(), "Ticket list size should be 1");
        assertTicketEquals(expectedTicket, actualTickets.getFirst());
        verify(ticketRepository, times(1)).findAllTicketsByUser_Id(userId);
    }

    @Test
    void shouldReturnEmptyTicketsForNonExistentUser() {
        final var nonExistentUserId = 999L;
        when(ticketRepository.findAllTicketsByUser_Id(nonExistentUserId)).thenReturn(List.of());

        var actualTickets = ticketService.getAllTicketsOfUser(nonExistentUserId);

        assertNotNull(actualTickets, "The returned ticket list should not be null");
        assertTrue(actualTickets.isEmpty(), "Ticket list should be empty");
        verify(ticketRepository, times(1)).findAllTicketsByUser_Id(nonExistentUserId);
    }

    @Test
    void shouldReturnTicketsForTrain() {
        final var expectedTicket = createTestTicket();
        final var trainNumber = expectedTicket.getTrain().getNumber();
        when(ticketRepository.findAllTicketsByTrain_Number(trainNumber)).thenReturn(List.of(expectedTicket));

        var actualTickets = ticketService.getAllTicketsOfTrain(trainNumber);

        assertNotNull(actualTickets, "The returned ticket list should not be null");
        assertEquals(1, actualTickets.size(), "Ticket list size should be 1");
        assertTicketEquals(expectedTicket, actualTickets.getFirst());
        verify(ticketRepository, times(1)).findAllTicketsByTrain_Number(trainNumber);
    }

    @Test
    void shouldReturnEmptyTicketsForNonExistentTrain() {
        final var nonExistentTrainNumber = "999";
        when(ticketRepository.findAllTicketsByTrain_Number(nonExistentTrainNumber)).thenReturn(List.of());

        var actualTickets = ticketService.getAllTicketsOfTrain(nonExistentTrainNumber);

        assertNotNull(actualTickets, "The returned ticket list should not be null");
        assertTrue(actualTickets.isEmpty(), "Ticket list should be empty");
        verify(ticketRepository, times(1)).findAllTicketsByTrain_Number(nonExistentTrainNumber);
    }

    private void assertTicketEquals(final Ticket expected, final TicketDTO actual) {
        assertEquals(expected.getPrice(), actual.getPrice(), "Ticket price should match");
        assertEquals(expected.getStatus(), actual.getStatus(), "Ticket status should match");
        assertEquals(expected.getUser().getPhoneNumber(), actual.getUser().getPhoneNumber(), "User phone number should match");
        assertEquals(expected.getTrain().getName(), actual.getTrain().getName(), "Train name should match");
    }

    static Ticket createTestTicket() {
        var now = LocalDateTime.now();
        return Ticket.builder()
                .user(buildTestUser())
                .train(buildTestTrain())
                .car(buildTestCar())
                .seat(buildTestSeat())
                .departureStation(buildRoute(now, "Station A"))
                .arrivalStation(buildRoute(now.plusHours(2), "Station B"))
                .price(100.0)
                .status(TicketStatus.BOOKED)
                .build();
    }


    private static User buildTestUser() {
        return User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .passwordHash("hashed_password")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(Role.USER)
                .build();
    }

    private static Train buildTestTrain() {
        return Train.builder()
                .number("123")
                .name("Test Train")
                .totalCars(5)
                .build();
    }

    private static Car buildTestCar() {
        return Car.builder()
                .train(buildTestTrain())
                .number(1)
                .type(CarType.COUPE)
                .totalSeats(36)
                .build();
    }

    private static Seat buildTestSeat() {
        return Seat.builder()
                .car(buildTestCar())
                .number(1)
                .type(SeatType.LOWER)
                .build();
    }

    private static Route buildRoute(LocalDateTime time, String stationName) {
        return Route.builder()
                .train(buildTestTrain())
                .station(buildStation(stationName))
                .stopOrder(1)
                .arrivalTime(time)
                .departureTime(time.plusHours(1))
                .build();
    }

    private static Station buildStation(String name) {
        return Station.builder()
                .name(name)
                .city("City " + name.charAt(name.length() - 1))
                .country("Country " + name.charAt(name.length() - 1))
                .build();
    }
}
