package com.llm.staa.service;

import com.llm.staa.tools.TicketStatusTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RagService {
    private final ChatClient chatClient;
    private final RetrievalService retrievalService;

    public RagService(ChatClient.Builder chatClientBuilder, RetrievalService retrievalService) {
        this.chatClient = chatClientBuilder.build();
        this.retrievalService = retrievalService;
    }

    private static final String SYSTEM_TEMPLATE = """
        You have access to tools. If the user's question can be answered by calling a tool (such as checking ticket status), use the tool directly and return its result.
        
        Otherwise, for general questions, please answer using only the context provided below. Do not give partial answer.
        If no answer found provide fallback message.
        Fallback message: "Sorry, unable to find feasible answer. Contact a Human Agent."

        Context:
        %s
        """;

    public String answer(String userQuery) {
        List<Document> retrievedDocs = retrievalService.retrieveSimilar(userQuery);

        String context = retrievedDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        String systemPrompt = SYSTEM_TEMPLATE.formatted(context);

        log.info("Sending prompt with {} retrieved chunks", retrievedDocs.size());
        // in small model tools and systemContext like llama 3.2
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userQuery)
                .call()
                .content();
    }

}
