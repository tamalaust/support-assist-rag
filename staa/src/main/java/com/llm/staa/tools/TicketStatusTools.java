package com.llm.staa.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TicketStatusTools {
    private static final Map<String, String> MOCK_TICKETS = Map.of(
            "1001", "Open",
            "1002", "In Progress",
            "1003", "Waiting on Customer",
            "1004", "Escalated",
            "1005", "Resolved",
            "1006", "Closed"
    );

    @Tool(description = "Get the current status of a support ticket given its ticket ID")
    public String getTicketStatus(String ticketId) {
        return MOCK_TICKETS.getOrDefault(ticketId, "No ticket found with ID " + ticketId);
    }
}
