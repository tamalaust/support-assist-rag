# Support Ticket AI Assistant(STAA)(RAG-based)

RAG-powered support ticket assistant built with Spring Boot — retrieves relevant docs via vector search and generates context-aware answers using LLM integration.

> 🚧 **Work in Progress** — actively being built. Full documentation, architecture diagram, and setup instructions coming soon.

## Planned Tech Stack

- **Backend:** Java, Spring Boot, Spring AI
- **LLM:** Ollama / Groq (free-tier)
- **Embeddings:** sentence-transformers (local)
- **Vector Store:** PostgreSQL + pgvector
- **Caching:** Redis
- **Search:** Elasticsearch (hybrid retrieval)
- **DevOps:** Docker, Docker Compose

## Roadmap

- [ ] LLM API integration with Spring Boot
- [ ] Embedding pipeline + pgvector setup
- [ ] Full RAG pipeline (retrieval + prompt construction + generation)
- [ ] Resilience (rate-limit handling, retries)
- [ ] Redis caching layer
- [ ] Elasticsearch hybrid search
- [ ] Dockerized deployment

## Status

Currently in early development. Star/watch this repo for updates.
