package com.llm.staa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrievalServiceTest {

    @Mock
    private VectorStore vectorStore;

    @InjectMocks
    private RetrievalService retrievalService;

    private static final String TEST_QUERY = "how do I reset my password";

    @BeforeEach
    void setUp() {
        // common setup if needed later
    }

    @Test
    void retrieveSimilar_shouldReturnDocuments_whenVectorStoreHasMatches() {
        // Arrange
        Document mockDoc = new Document("To reset your password, go to Forgot Password...");
        List<Document> expectedDocs = List.of(mockDoc);
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(expectedDocs);

        // Act
        List<Document> result = retrievalService.retrieveSimilar(TEST_QUERY);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getText()).contains("reset your password");
    }

    @Test
    void retrieveSimilar_shouldReturnEmptyList_whenNoMatchesFound() {
        // Arrange
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Collections.emptyList());

        // Act
        List<Document> result = retrievalService.retrieveSimilar("completely unrelated query about weather");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void retrieveSimilar_shouldBuildSearchRequestWithCorrectTopKAndThreshold() {
        // Arrange
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Collections.emptyList());
        ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);

        // Act
        retrievalService.retrieveSimilar(TEST_QUERY);

        // Assert
        verify(vectorStore).similaritySearch(captor.capture());
        SearchRequest capturedRequest = captor.getValue();

        assertThat(capturedRequest.getTopK()).isEqualTo(4);
        assertThat(capturedRequest.getSimilarityThreshold()).isEqualTo(0.5);
        assertThat(capturedRequest.getQuery()).isEqualTo(TEST_QUERY);
    }
}