package com.llm.staa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private RetrievalService retrievalService;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    private RagService ragService;

    private static final String TEST_QUERY = "how do I reset my password";

    @Test
    void answer_shouldIncludeRetrievedContextInPrompt() {
        // Arrange
        when(chatClientBuilder.build()).thenReturn(chatClient);
        ragService = new RagService(chatClientBuilder, retrievalService);

        Document doc = new Document("To reset your password, go to Forgot Password page...");
        when(retrievalService.retrieveSimilar(TEST_QUERY)).thenReturn(List.of(doc));

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("You can reset your password by...");

        // Act
        String result = ragService.answer(TEST_QUERY);

        // Assert
        assertThat(result).isEqualTo("You can reset your password by...");
        verify(requestSpec).user(TEST_QUERY);
    }
}