package com.llm.staa.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrievalService {

    private final VectorStore vectorStore;

    private static final int TOP_K = 4;
    private static final double SIMILARITY_THRESHOLD = 0.5;
    @Retry(name = "ollamaEmbedding")
    @CircuitBreaker(name = "ollamaEmbedding", fallbackMethod = "retrievalFallback")
    public List<Document> retrieveSimilar(String query) {
        log.info("Retrieving similar chunks for query: {}", query);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(TOP_K)
                .similarityThreshold(SIMILARITY_THRESHOLD)
                .build();

        List<Document> results = vectorStore.similaritySearch(searchRequest);

        log.info("Found {} similar chunks", results.size());
        return results;
    }
    private List<Document> retrievalFallback(String query, Throwable t) {
        log.error("Vector store/embedding unreachable: {}", t.getMessage());
        return Collections.emptyList();
    }
}