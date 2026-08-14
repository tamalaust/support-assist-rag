package com.llm.staa.config;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestionRunner implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final EntityManager entityManager;


    @Override
    @Transactional // may create challenge for distributed deployment may look into further if time permits
    public void run(String... args) throws Exception {
        log.info("Truncating vector_store table...");
        entityManager.createNativeQuery("TRUNCATE TABLE vector_store").executeUpdate();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:embeddble_docs/*.txt");

        List<Document> allChunks = new ArrayList<>();
        TextSplitter splitter = TokenTextSplitter.builder().build();

        for (Resource resource : resources) {
            TextReader textReader = new TextReader(resource);
            List<Document> documents = textReader.get();
            allChunks.addAll(splitter.apply(documents));
        }

        log.info("Ingesting {} chunks...", allChunks.size());
        vectorStore.add(allChunks);
    }
}
