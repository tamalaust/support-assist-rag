package com.llm.staa.controller;

import com.llm.staa.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RetrievalController {

    private final RetrievalService retrievalService;

    @GetMapping("/retrieve")
    public List<Document> retrieve(@RequestParam String query) {
        return retrievalService.retrieveSimilar(query);
    }
}