package com.llm.staa.service;

import com.llm.staa.tools.TicketStatusTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketService {

    private final ChatClient chatClient;

    public TicketService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getTicketStatus(String userQuery) {
        log.info("Handling ticket query: {}", userQuery);
        return chatClient.prompt()
                .tools(new TicketStatusTools())
                .user(userQuery)
                .call()
                .content();
    }
}