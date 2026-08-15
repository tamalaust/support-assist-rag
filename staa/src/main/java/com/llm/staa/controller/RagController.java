package com.llm.staa.controller;

import com.llm.staa.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @GetMapping("/ask")
    public String ask(@RequestParam String query) {
        return ragService.answer(query);
    }
}
