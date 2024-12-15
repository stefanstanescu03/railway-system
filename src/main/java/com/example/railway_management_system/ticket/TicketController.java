package com.example.railway_management_system.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/ticket")
public class TicketController {

        private final TicketService ticketService;

        @Autowired
        public TicketController(TicketService ticketService) {
            this.ticketService = ticketService;
        }

        // debugging purposes this should not exist!
        @GetMapping
        public List<Ticket> getTickets() {
            return ticketService.getTickets();
        }

}
