package com.llm.staa.service;

import com.llm.staa.tools.TicketStatusTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final LlmInvocationService llmInvocationService;

    public String getStatus(String userQuery) {
        return llmInvocationService.callWithTools(userQuery, new TicketStatusTools());
    }
}