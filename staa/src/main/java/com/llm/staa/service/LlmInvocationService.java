package com.llm.staa.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmInvocationService {

    private final ChatClient chatClient;

    @Retry(name = "ollamaChat")
    @CircuitBreaker(name = "ollamaChat", fallbackMethod = "fallback")
    public String call(String systemPrompt, String userQuery) {
        log.info("Calling Ollama (chat)...");
        return chatClient.prompt()
                .system(systemPrompt != null ? systemPrompt : "")
                .user(userQuery)
                .call()
                .content();
    }

    @Retry(name = "ollamaChat")
    @CircuitBreaker(name = "ollamaChat", fallbackMethod = "fallbackWithTools")
    public String callWithTools(String userQuery, Object... tools) {
        log.info("Calling Ollama (tool-calling)...");
        return chatClient.prompt()
                .tools(tools)
                .user(userQuery)
                .call()
                .content();
    }

    private String fallback(String systemPrompt, String userQuery, Throwable t) {
        log.error("Ollama unreachable after retries: {}", t.getMessage());
        return "Sorry, our AI service is temporarily unavailable. Please try again in a moment or contact a Human Agent.";
    }

    private String fallbackWithTools(String userQuery, Object[] tools, Throwable t) {
        log.error("Ollama unreachable after retries (tool-calling): {}", t.getMessage());
        return "Sorry, our AI service is temporarily unavailable. Please try again in a moment or contact a Human Agent.";
    }
}