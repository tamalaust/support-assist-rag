package com.llm.staa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagServiceTest {

    @Mock
    private LlmInvocationService llmInvocationService;

    @Mock
    private RetrievalService retrievalService;

    private RagService ragService;

    private static final String TEST_QUERY = "how do I reset my password";

    @Test
    void answer_shouldIncludeRetrievedContextInPrompt() {
        // Arrange
        ragService = new RagService(llmInvocationService, retrievalService);

        Document doc = new Document("To reset your password, go to Forgot Password page...");
        when(retrievalService.retrieveSimilar(TEST_QUERY)).thenReturn(List.of(doc));
        when(llmInvocationService.call(anyString(), eq(TEST_QUERY)))
                .thenReturn("You can reset your password by...");

        // Act
        String result = ragService.answer(TEST_QUERY);

        // Assert
        assertThat(result).isEqualTo("You can reset your password by...");

        ArgumentCaptor<String> systemPromptCaptor = ArgumentCaptor.forClass(String.class);
        verify(llmInvocationService).call(systemPromptCaptor.capture(), eq(TEST_QUERY));
        assertThat(systemPromptCaptor.getValue()).contains("reset your password");
        assertThat(systemPromptCaptor.getValue()).contains("Please answer using only the context provided");
    }

    @Test
    void answer_shouldStillCallLlm_whenNoContextRetrieved() {
        // Arrange
        ragService = new RagService(llmInvocationService, retrievalService);

        when(retrievalService.retrieveSimilar(anyString())).thenReturn(Collections.emptyList());
        when(llmInvocationService.call(anyString(), anyString()))
                .thenReturn("Sorry, unable to find feasible answer. Contact a Human Agent.");

        // Act
        String result = ragService.answer("what's the weather today");

        // Assert
        assertThat(result).isEqualTo("Sorry, unable to find feasible answer. Contact a Human Agent.");
    }
}