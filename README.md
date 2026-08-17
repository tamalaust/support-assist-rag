# Support Ticket AI Assistant (STAA) (RAG-based)

RAG-powered support ticket assistant built with Spring Boot — retrieves relevant docs via vector search and generates context-aware answers using LLM integration.

## Scope of This Repo

This repo covers the **core RAG pipeline only** — LLM integration, embeddings, vector retrieval, and prompt construction. Caching, hybrid search, and multi-model routing have been split into separate, independently-deployable modules (see [Related Modules](#related-modules)) that integrate with this service over REST.

## Tech Stack

- **Backend:** Java, Spring Boot, Spring AI
- **LLM:** Ollama (llama3.2)
- **Embeddings:** sentence-transformers (local)
- **Vector Store:** PostgreSQL + pgvector
- **DevOps:** Docker, Docker Compose

## Roadmap

- [x] LLM API integration with Spring Boot
- [x] Embedding pipeline + pgvector setup
- [x] Document ingestion + retrieval endpoint
- [x] Full RAG pipeline (retrieval + prompt construction + generation)
- [x] Resilience (rate-limit handling, retries)
- [x] Unit test coverage (happy path)
- [x] README
- [x] architecture diagram 
- [x] API examples
- [x] Dockerized deployment

## Related Modules

These are separate repos that plug into this service via REST once ready — not part of this repo's scope:

| Module | Purpose | Integration point |
|---|---|---|
| `ai-cache-gateway` | Redis-based cache-aside layer for LLM responses | Wraps the LLM call in this service |
| `ai-observability-logger` | Logs latency, token count, retrieval hit/miss | This service sends async log events |
| `llm-router` | Routes queries to Ollama/Groq by complexity | Will replace the direct LLM call here |
| `doc-extract-service` | Structured extraction from PDFs (standalone) | Future: feeds this service's ingestion pipeline |

## Status

Currently in early development. Star/watch this repo for updates.

## Known Limitations / Future Enhancements
- Chunk overlap not yet implemented (Spring AI's `TokenTextSplitter` doesn't support it natively) — would improve retrieval accuracy for content near chunk boundaries
- Local model (llama3.2 via Ollama) does not reliably combine RAG context-restriction instructions with tool-calling in a single prompt — verified isolated tool-calling and RAG work correctly independently, but combining them causes inconsistent behavior (raw JSON output or incorrect fallback). This is a known limitation of smaller local models; larger cloud models (e.g. Groq, GPT-4) typically handle this better. Kept as two separate flows for MVP scope.
