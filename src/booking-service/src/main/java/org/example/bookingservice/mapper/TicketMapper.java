package org.example.bookingservice.mapper;

import org.example.bookingservice.dto.TicketDTO;
import org.example.bookingservice.model.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketDTO toTicketDTO(Ticket ticket);
    Ticket toTicket(TicketDTO ticketDTO);
}
